package tech.ignission.openvidu4s.akka.interpreters

import akka.actor.ActorSystem
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.BasicHttpCredentials
import akka.http.scaladsl.Http
import monix.eval.Task
import org.slf4j.LoggerFactory
import tech.ignission.openvidu4s.core.Basic
import tech.ignission.openvidu4s.core.dsl.{HttpDSL, HttpQuery, RequestError, ServerDown}
import tech.ignission.openvidu4s.core.dsl.OpenViduHttpDsl.Response
import spray.json._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import tech.ignission.openvidu4s.core.dsl.AlreadyExists

class OpenViduHttpDSLOnAkka()(implicit actorSystem: ActorSystem, exc: ExecutionContext)
    extends HttpDSL[Task] {

  private val logger = LoggerFactory.getLogger(getClass)

  private val http    = Http()
  private val timeout = 10.seconds
  private val reqHeaders: Seq[HttpHeader] = Seq(
    headers.`User-Agent`("openvidu-client"),
    headers.`Accept-Charset`(HttpCharsets.`UTF-8`)
  )

  def terminate(): Task[Unit] =
    Task.deferFuture(http.shutdownAllConnectionPools())

  override def get[A](query: HttpQuery)(implicit format: JsonFormat[A]): Task[Response[A]] =
    for {
      serverResponse <- doRequest(createRequest(HttpMethods.GET, query))
      response = serverResponse.map(_.parseJson.convertTo[A](format))
    } yield response

  override def post[Body, A](query: HttpQuery, body: Body)(implicit
      format: JsonReader[A],
      bodyFormat: JsonWriter[Body]
  ): Task[Response[A]] =
    for {
      serverResponse <- doRequest(createRequest(HttpMethods.POST, query, body, bodyFormat))
      response = serverResponse.map { content =>
        if (content.isEmpty) "{}" else content
      }.map(_.parseJson.convertTo[A](format))
    } yield response

  private def createRequest(
      method: HttpMethod,
      query: HttpQuery,
      optBody: Option[String] = None
  ): HttpRequest =
    query.credentials match {
      case Basic(username, password) =>
        val uri = Uri(query.baseUrl + query.path)
        logger.info(s"Create HTTP request method: ${method.value} and uri: $uri")
        optBody.map { body =>
          HttpRequest(
            method = method,
            uri = uri,
            entity = HttpEntity(ContentTypes.`application/json`, body)
          )
        }.getOrElse {
          HttpRequest(
            method = method,
            uri = uri
          )
        }.withHeaders(reqHeaders :+ createAuthHeader(username = username, password = password))
    }

  private def createRequest[Body](
      method: HttpMethod,
      query: HttpQuery,
      body: Body,
      format: JsonWriter[Body]
  ): HttpRequest = {
    logger.info(s"Prepare request with body $body")

    createRequest(method, query, Some(body.toJson(format).compactPrint))
  }

  private def createAuthHeader(username: String, password: String): HttpHeader =
    headers.Authorization(BasicHttpCredentials(username = username, password = password))

  private def doRequest(request: HttpRequest): Task[Response[String]] = {
    logger.info(s"Execute request $request")
    for {
      response <- Task.deferFuture(http.singleRequest(request))
      data     <- Task.deferFuture(response.entity.toStrict(timeout).map(_.data.utf8String))
      result = {
        val status = response.status.intValue()
        logger.info(s"Received response with status: $status")
        if (response.status.isFailure()) {
          if (status == 409)
            Left(AlreadyExists)
          else if (status >= 400 && status < 500)
            Left(RequestError(data))
          else {
            Left(ServerDown)
          }
        } else {
          logger.info(s"Response data is $data")
          Right(data)
        }
      }
    } yield result
  }
}

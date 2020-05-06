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

class OpenViduHttpDSLOnAkka()(implicit actorSystem: ActorSystem, exc: ExecutionContext)
    extends HttpDSL[Task] {

  private val logger = LoggerFactory.getLogger(getClass)

  private val http    = Http()
  private val timeout = 10.seconds
  private val reqHeaders: Seq[HttpHeader] = Seq(
    headers.`User-Agent`("jira4s"),
    headers.`Accept-Charset`(HttpCharsets.`UTF-8`)
  )

  def terminate(): Task[Unit] =
    Task.deferFuture(http.shutdownAllConnectionPools())

  override def get[A](query: HttpQuery)(implicit format: JsonFormat[A]): Task[Response[A]] =
    for {
      serverResponse <- doRequest(createRequest(HttpMethods.GET, query))
      response = serverResponse.map(_.parseJson.convertTo[A](format))
    } yield response

  private def createRequest(method: HttpMethod, query: HttpQuery): HttpRequest =
    query.credentials match {
      case Basic(username, password) =>
        val uri = Uri(query.baseUrl + query.path)
        logger.info(s"Create HTTP request method: ${method.value} and uri: $uri")
        HttpRequest(
          method = method,
          uri = uri
        ).withHeaders(reqHeaders :+ createAuthHeader(username = username, password = password))
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
          if (status >= 400 && status < 500)
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

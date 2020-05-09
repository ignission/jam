package emoi.server.rest.routes

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Route, StandardRoute}
import emoi.server.dsl.{AppError, OpenViduClientError, RestDSL}
import monix.eval.Task
import monix.execution.Scheduler
import spray.json._
import tech.ignission.openvidu4s.core.dsl.{AlreadyExists, RequestError, ServerDown}

class ApiRoute(restDSL: RestDSL[Task])(implicit s: Scheduler) {
  import emoi.server.rest.formatters.SprayJsonFormats._

  implicit class ResponseHandler[A](result: Task[Either[AppError, A]]) {
    def handleResponse(implicit s: Scheduler, formatter: JsonWriter[A]): StandardRoute = {
      val f = result.map {
        case Right(value) =>
          HttpResponse(StatusCodes.OK, entity = value.toJson.compactPrint)
        case Left(error: OpenViduClientError) =>
          error.inner match {
            case AlreadyExists =>
              HttpResponse(StatusCodes.Conflict)
            case RequestError(msg) =>
              HttpResponse(StatusCodes.BadRequest, entity = msg)
            case ServerDown =>
              HttpResponse(StatusCodes.BadGateway)
          }
        case Left(_: emoi.server.dsl.InternalError) =>
          HttpResponse(StatusCodes.InternalServerError)
      }.runToFuture

      complete(f)
    }
  }

  def routes: Route =
    pathPrefix("rest" / "api" / "v1") {
      sessionRoutes(restDSL) ~ tokenRoutes
    } ~ defaultRoute

  private def sessionRoutes(restDSL: RestDSL[Task]): Route =
    path("sessions") {
      get {
        restDSL.listSessions.handleResponse
      }
    }

  private def tokenRoutes: Route =
    pathPrefix("tokens" / ".+".r) { sessionId =>
      post {
        complete("post")
      }
    }

  private def defaultRoute: Route =
    pathPrefix(".+".r) { _ =>
      complete(HttpResponse(404))
    }
}

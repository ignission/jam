package jam.rest.routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Route, StandardRoute}
import cats.Monad
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import io.getquill.{MysqlMonixJdbcContext, SnakeCase}
import monix.eval.Task
import monix.execution.Scheduler
import spray.json._

import jam.application.{AppError, AppModule, OpenViduClientError}
import jam.dsl.RestDSL

import tech.ignission.openvidu4s.core.datas.SessionId
import tech.ignission.openvidu4s.core.dsl.{AlreadyExists, RequestError, ServerDown}

class ApiRoute(
    restDSL: RestDSL[Task],
    appModule: AppModule[Task, MysqlMonixJdbcContext[SnakeCase]]
)(implicit s: Scheduler)
    extends SprayJsonSupport
    with DefaultJsonProtocol {
  import jam.rest.formatters.SprayJsonFormats._
  import jam.rest.routes.ResponseHandler._

  private val authRoutes = new AuthRoutes(appModule.accountModule)

  def routes: Route =
    cors() {
      pathPrefix("rest" / "api" / "v1") {
        sessionRoutes(restDSL) ~ tokenRoutes ~ authRoutes.routes
      } ~ defaultRoute
    }

  private def sessionRoutes(restDSL: RestDSL[Task]): Route =
    path("sessions") {
      concat(
        get {
          restDSL.listSessions.handleResponse.toRoute
        },
        post {
          entity(as[CreateSessionRequest]) { req =>
            restDSL.createSession(req.sessionId).handleResponse.toRoute
          }
        }
      )
    }

  private def tokenRoutes: Route =
    pathPrefix("tokens" / ".+".r) { sessionId =>
      post {
        restDSL.generateToken(SessionId(sessionId)).handleResponse.toRoute
      }
    }

  private def defaultRoute: Route =
    pathPrefix(".+".r) { _ =>
      complete(HttpResponse(404))
    }
}

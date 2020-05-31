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

import jam.application.Result.Result
import jam.application.sessions.SessionModule
import jam.application.{AppError, AppModule, OpenViduClientError}

import tech.ignission.openvidu4s.core.datas.{GeneratedToken, InitializedSession, Session, SessionId}
import tech.ignission.openvidu4s.core.dsl.{AlreadyExists, RequestError, ServerDown}

class ApiRoute[Ctx](
    appModule: AppModule[Task, Ctx]
)(implicit s: Scheduler)
    extends SprayJsonSupport
    with DefaultJsonProtocol {
  import jam.rest.formatters.SprayJsonFormats._
  import jam.application.dsl.syntax._
  import jam.rest.routes.ResponseHandler._

  private val authRoutes = new AuthRoutes(appModule.accountModule)

  def routes: Route =
    cors() {
      pathPrefix("rest" / "api" / "v1") {
        sessionRoutes(appModule.sessionModule) ~ tokenRoutes(
          appModule.sessionModule
        ) ~ authRoutes.routes
      } ~ defaultRoute
    }

  private def sessionRoutes(module: SessionModule[Task]): Route = {
    val service = module.sessionService

    path("sessions") {
      concat(
        get {
          val taskResult: Task[Result[Seq[Session]]] = service.listSessions

          taskResult.handleResponse.toRoute
        },
        post {
          entity(as[CreateSessionRequest]) { req =>
            val taskResult: Task[Result[InitializedSession]] = service.createSession(req.sessionId)

            taskResult.handleResponse.toRoute
          }
        }
      )
    }
  }

  private def tokenRoutes(module: SessionModule[Task]): Route = {
    val service = module.sessionService

    pathPrefix("tokens" / ".+".r) { sessionId =>
      post {
        val taskResult: Task[Result[GeneratedToken]] = service.generateToken(SessionId(sessionId))

        taskResult.handleResponse.toRoute
      }
    }
  }

  private def defaultRoute: Route =
    pathPrefix(".+".r) { _ =>
      complete(HttpResponse(404))
    }
}

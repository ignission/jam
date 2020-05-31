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
import jam.rest.routes.api.{AuthRoutes, SessionRoutes}

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

  private val authRoutes    = new AuthRoutes(appModule.accountModule)
  private val sessionRoutes = new SessionRoutes(appModule.sessionModule)

  def routes: Route =
    cors() {
      pathPrefix("rest" / "api" / "v1") {
        sessionRoutes.routes ~ authRoutes.routes
      } ~ defaultRoute
    }

  private def defaultRoute: Route =
    pathPrefix(".+".r) { _ =>
      complete(HttpResponse(404))
    }
}

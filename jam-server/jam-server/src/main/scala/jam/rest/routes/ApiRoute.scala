package jam.rest.routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import monix.eval.Task
import monix.execution.Scheduler
import spray.json._

import jam.application.AppModule
import jam.rest.routes.api.{AuthRoutes, SessionRoutes}

class ApiRoute[Ctx](
    appModule: AppModule[Task, Ctx]
)(implicit s: Scheduler)
    extends SprayJsonSupport
    with DefaultJsonProtocol {

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

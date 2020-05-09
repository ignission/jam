package emoi.server.rest.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model.HttpResponse
import tech.ignission.openvidu4s.core.apis.AllAPI
import monix.eval.Task

object ApiRoute {
  def routes(openviduAPI: AllAPI[Task]): Route =
    pathPrefix("rest" / "api" / "v1") {
      sessionRoutes ~ tokenRoutes
    } ~ defaultRoute

  private def sessionRoutes: Route =
    path("sessions") {
      get {
        complete("get session")
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

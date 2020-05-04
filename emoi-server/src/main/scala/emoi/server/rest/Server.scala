package emoi.server.rest

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.actor.ActorSystem
import scala.concurrent.Future
import akka.http.scaladsl.server.Route

object Server {
  def start(interface: String, port: Int)(implicit
      system: ActorSystem
  ): Future[Http.ServerBinding] = {
    val routes = htmlRoute ~ jsRoutes ~ imageRoutes ~ defaultRoute ~ path("ping") {
      get {
        complete("pong")
      }
    }

    Http().bindAndHandle(routes, interface, port)
  }

  private def htmlRoute: Route =
    get {
      pathEndOrSingleSlash {
        getFromResource("static/index.html")
      }
    }

  private def jsRoutes: Route =
    pathPrefix(".+.js".r) { str =>
      get {
        getFromResource(s"static/$str")
      }
    }

  private def imageRoutes: Route =
    pathPrefix("images" / ".+".r) { str =>
      get {
        getFromResource(s"static/images/$str")
      }
    }

  private def defaultRoute: Route =
    pathPrefix(".+".r) { _ =>
      get {
        getFromResource("static/index.html")
      }
    }
}

package emoi.server.rest

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.actor.ActorSystem
import scala.concurrent.Future
import akka.http.scaladsl.server.Route

object Server {
  def start(host: String, port: Int)(implicit
      system: ActorSystem
  ): Future[Http.ServerBinding] = {
    val routes =
      htmlRoute ~ assetsRoutes ~
        path("ping") {
          get {
            complete("pong")
          }
        }

    Http().bindAndHandle(routes, host, port)
  }

  private def htmlRoute: Route =
    get {
      pathEndOrSingleSlash {
        getFromResource("static/index.html")
      }
    }

  private def assetsRoutes: Route =
    path("index.js") {
      get {
        getFromResource("static/index.js")
      }
    }
}

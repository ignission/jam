package emoi.server.rest

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.actor.ActorSystem
import scala.concurrent.Future

object Server {
  def start(interface: String, port: Int)(implicit
      system: ActorSystem
  ): Future[Http.ServerBinding] = {
    val routes =
      StaticRoute.routes ~ StaticRoute.defaultRoute ~ ApiRoute.routes

    Http().bindAndHandle(routes, interface, port)
  }

}

package jam.websocket.server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._

import scala.concurrent.Future

object Server {

  def start(interface: String, port: Int)(implicit
      system: ActorSystem
  ): Future[Http.ServerBinding] = {
    val routes = path("connect" / Segment) { _ =>
      cors() {
        ???
      }
    }

    Http().bindAndHandle(routes, interface, port)
  }
}
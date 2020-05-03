package emoi.server.rest

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import akka.actor.ActorSystem
import scala.concurrent.Future

object Server {
  def start(interface: String, port: Int)(implicit
      mat: Materializer,
      system: ActorSystem
  ): Future[Http.ServerBinding] = {
    val routes = path("ping") {
      get {
        complete("pong")
      }
    }

    Http().bindAndHandle(routes, interface, port)
  }
}

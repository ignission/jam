package emoi.server.rest

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import emoi.server.rest.routes.{ApiRoute, StaticRoute}

import scala.concurrent.Future
import tech.ignission.openvidu4s.core.apis.AllAPI
import monix.eval.Task

object Server {
  def start(interface: String, port: Int, openviduAPI: AllAPI[Task])(implicit
      system: ActorSystem
  ): Future[Http.ServerBinding] = {
    val routes =
      StaticRoute.routes ~ StaticRoute.defaultRoute ~ ApiRoute.routes(openviduAPI)

    Http().bindAndHandle(routes, interface, port)
  }

}

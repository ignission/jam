package emoi.server.rest

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import emoi.server.rest.routes.{ApiRoute, StaticRoute}
import tech.ignission.openvidu4s.core.apis.AllAPI
import monix.eval.Task
import monix.execution.Scheduler

import scala.concurrent.Future

object Server {
  def start(interface: String, port: Int, openviduAPI: AllAPI[Task])(implicit
      system: ActorSystem,
      s: Scheduler
  ): Future[Http.ServerBinding] = {
    val routes =
      StaticRoute.routes ~ ApiRoute.routes(openviduAPI) ~ StaticRoute.defaultRoute

    Http().bindAndHandle(routes, interface, port)
  }

}

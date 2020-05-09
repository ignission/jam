package emoi.server.rest

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import emoi.server.dsl.RestDSL
import emoi.server.rest.routes.{ApiRoute, StaticRoute}
import monix.eval.Task
import monix.execution.Scheduler

import scala.concurrent.Future

object Server {
  def start(interface: String, port: Int, restDSL: RestDSL[Task])(implicit
      system: ActorSystem,
      s: Scheduler
  ): Future[Http.ServerBinding] = {
    val routes =
      StaticRoute.routes ~ new ApiRoute(restDSL).routes ~ StaticRoute.defaultRoute

    Http().bindAndHandle(routes, interface, port)
  }

}

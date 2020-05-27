package jam.rest

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import cats.Monad
import monix.eval.Task
import monix.execution.Scheduler

import jam.domains.auth.AccountRepository
import jam.dsl.RestDSL
import jam.infrastructure.persistence.interpreters.mysql.types._
import jam.rest.routes.{ApiRoute, StaticRoute}

import scala.concurrent.Future

object Server {
  def start[F[_]: Monad: AccountRepository](interface: String, port: Int, restDSL: RestDSL[Task])(
      implicit
      system: ActorSystem,
      s: Scheduler
  ): Future[Http.ServerBinding] = {
    val routes =
      StaticRoute.routes ~ new ApiRoute(restDSL).routes ~ StaticRoute.defaultRoute

    Http().bindAndHandle(routes, interface, port)
  }

}

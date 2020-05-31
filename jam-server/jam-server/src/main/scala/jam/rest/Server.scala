package jam.rest

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import cats.Monad
import io.getquill.{MysqlMonixJdbcContext, SnakeCase}
import monix.eval.Task
import monix.execution.Scheduler

import jam.application.AppModule
import jam.dsl.RestDSL
import jam.rest.routes.{ApiRoute, StaticRoute}

import scala.concurrent.Future

object Server {
  def start(
      interface: String,
      port: Int,
      restDSL: RestDSL[Task],
      appModule: AppModule[Task, MysqlMonixJdbcContext[SnakeCase]]
  )(implicit
      system: ActorSystem,
      s: Scheduler
  ): Future[Http.ServerBinding] = {
    val routes =
      StaticRoute.routes ~ new ApiRoute(restDSL, appModule).routes ~ StaticRoute.defaultRoute

    Http().bindAndHandle(routes, interface, port)
  }

}

package jam.server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import io.getquill.context.monix.Runner
import io.getquill.{MysqlMonixJdbcContext, SnakeCase}
import monix.eval.Task
import monix.execution.Scheduler

import jam.application.accounts.{AccountModule, AccountService}
import jam.application.dsl.Result.Result
import jam.application.sessions.{SessionModule, SessionService}
import jam.application.{AppError, AppModule, InternalError}
import jam.infrastructure.interpreters.AuthInterpreter
import jam.infrastructure.persistence.interpreters.mysql.ops.AccountTableOps

import tech.ignission.openvidu4s.akka.interpreters.OpenViduHttpDSLOnAkka
import tech.ignission.openvidu4s.core.Basic
import tech.ignission.openvidu4s.core.apis.AllAPI

import scala.util.control.NonFatal

object App {
  import jam.application.dsl.syntax._

  type TaskResult[A] = Result[Task, AppError, A]

  implicit val system: ActorSystem = ActorSystem()
  implicit val exc: Scheduler      = monix.execution.Scheduler.Implicits.global
  implicit val ctx: MysqlMonixJdbcContext[SnakeCase] =
    new MysqlMonixJdbcContext(SnakeCase, "ctx", Runner.using(exc))

  def main(args: Array[String]): Unit = {

    val mode              = Config.mode
    val serverConfig      = Config.OpenVidu.Server
    val akkaHttpDSL       = new OpenViduHttpDSLOnAkka(debug = mode == Mode.Local || mode == Mode.Test)
    val credential        = Basic(serverConfig.username, serverConfig.password)
    val openviduAPI       = new AllAPI(serverConfig.url, credential)(akkaHttpDSL)
    val accountRepository = AccountTableOps
    val authInterpreter   = new AuthInterpreter
    val appModule = new AppModule[Task, MysqlMonixJdbcContext[SnakeCase]] {
      override val accountModule: AccountModule[Task, MysqlMonixJdbcContext[SnakeCase]] =
        new AccountModule[Task, MysqlMonixJdbcContext[SnakeCase]] {
          override val accountService: AccountService[Task, MysqlMonixJdbcContext[SnakeCase]] =
            new AccountService(accountRepository, authInterpreter)
        }
      override val sessionModule: SessionModule[Task] =
        new SessionModule[Task] {
          override val sessionService: SessionService[Task] =
            new SessionService(openviduAPI)
        }
    }

    val startupTask = for {
      bindings <- startServer(Config.interface, Config.port, appModule).handleError
    } yield bindings

    startupTask.value
      .map {
        case Left(error) =>
          error.printStackTrace()
          system.terminate()
        case Right(_) =>
          println(s"Listening on port ${Config.port}")
      }
      .onErrorRecover {
        case NonFatal(ex) =>
          ex.printStackTrace()
          system.terminate()
      }
      .runAsyncAndForget

  }

  private def startServer(
      interface: String,
      port: Int,
      appModule: AppModule[Task, MysqlMonixJdbcContext[SnakeCase]]
  ): TaskResult[Http.ServerBinding] =
    Task.deferFuture {
      Server.start(interface, port, appModule).map(Right(_)).recover {
        case NonFatal(ex) =>
          Left(InternalError(ex): AppError)
      }
    }
}

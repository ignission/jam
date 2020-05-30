package jam

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import io.getquill.context.monix.Runner
import io.getquill.{MysqlMonixJdbcContext, SnakeCase}
import monix.eval.Task

import jam.application.Result.Result
import jam.application.accounts.{AccountModule, AccountRepository, AccountService}
import jam.application.{AppError, AppModule, InternalError}
import jam.dsl.RestDSL
import jam.infrastructure.persistence.interpreters.mysql.ops.AccountTableOps
import jam.interpreters.RestInterpreter
import jam.rest.Server

import tech.ignission.openvidu4s.akka.interpreters.OpenViduHttpDSLOnAkka
import tech.ignission.openvidu4s.core.Basic
import tech.ignission.openvidu4s.core.apis.AllAPI

import scala.util.control.NonFatal

object App {
  import dsl.syntax._

  type TaskResult[A] = Task[Result[A]]

  implicit val system = ActorSystem()
  implicit val exc    = monix.execution.Scheduler.Implicits.global
  implicit val ctx: MysqlMonixJdbcContext[SnakeCase] =
    new MysqlMonixJdbcContext(SnakeCase, "ctx", Runner.using(exc))

  private val accountRepository = AccountTableOps
  private val appModule = new AppModule[Task, MysqlMonixJdbcContext[SnakeCase]] {
    override val accountModule: AccountModule[Task, MysqlMonixJdbcContext[SnakeCase]] =
      new AccountModule[Task, MysqlMonixJdbcContext[SnakeCase]] {
        override val accountService: AccountService[Task, MysqlMonixJdbcContext[SnakeCase]] =
          new AccountService(accountRepository)
      }
  }

  private def startServer(
      interface: String,
      port: Int,
      restDSL: RestDSL[Task]
  ): TaskResult[Http.ServerBinding] =
    Task.deferFuture {
      Server.start(interface, port, restDSL, appModule).map(Right(_)).recover {
        case NonFatal(ex) =>
          Left(InternalError(ex): AppError)
      }
    }

  def main(args: Array[String]): Unit = {

    val mode         = Config.mode
    val serverConfig = Config.OpenVidu.Server
    val akkaHttpDSL  = new OpenViduHttpDSLOnAkka(debug = mode == Mode.Local || mode == Mode.Test)
    val credential   = Basic(serverConfig.username, serverConfig.password)
    val openviduAPI  = new AllAPI(serverConfig.url, credential)(akkaHttpDSL)
    val restDSL      = new RestInterpreter(openviduAPI)

    val startupTask = for {
      _        <- restDSL.listSessions.handleError
      bindings <- startServer(Config.interface, Config.port, restDSL).handleError
    } yield bindings

    startupTask.value.map {
      case Left(error) =>
        error.printStackTrace()
        system.terminate()
      case Right(_) =>
        println(s"Listening on port ${Config.port}")
    }.onErrorRecover {
      case NonFatal(ex) =>
        ex.printStackTrace()
        system.terminate()
    }.runAsyncAndForget

  }
}

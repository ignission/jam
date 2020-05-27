package jam

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import monix.eval.Task

import jam.domains.auth.AccountRepository
import jam.dsl.{AppError, InternalError, RestDSL}
import jam.infrastructure.persistence.interpreters.mysql.ops.AccountTableOps
import jam.infrastructure.persistence.interpreters.mysql.types._
import jam.interpreters.RestInterpreter
import jam.rest.Server

import tech.ignission.openvidu4s.akka.interpreters.OpenViduHttpDSLOnAkka
import tech.ignission.openvidu4s.core.Basic
import tech.ignission.openvidu4s.core.apis.AllAPI

import scala.util.control.NonFatal
object App {
  import dsl.syntax._

  implicit val system = ActorSystem()
  implicit val exc    = monix.execution.Scheduler.Implicits.global

  type Result[A]     = Either[AppError, A]
  type TaskResult[A] = Task[Result[A]]

  implicit val accountRepository: AccountRepository[Query] = AccountTableOps

  private def startServer(
      interface: String,
      port: Int,
      restDSL: RestDSL[Task]
  ): TaskResult[Http.ServerBinding] =
    Task.deferFuture {
      Server.start[Query](interface, port, restDSL).map(Right(_)).recover {
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

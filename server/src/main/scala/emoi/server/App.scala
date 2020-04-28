package emoi.server

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import emoi.server.dsl.{AppError, InternalError}
import monix.eval.Task
import emoi.server.rest.Server
import akka.http.scaladsl.Http
import scala.util.control.NonFatal

object App {
  import dsl.syntax._

  implicit val system       = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val exc          = monix.execution.Scheduler.Implicits.global

  type Result[A]     = Either[AppError, A]
  type TaskResult[A] = Task[Result[A]]

  private def startServer(host: String, port: Int): TaskResult[Http.ServerBinding] =
    Task.deferFuture {
      Server.start(host, port).map(Right(_)).recover {
        case NonFatal(ex) =>
          Left(InternalError(ex): AppError)
      }
    }

  def main(args: Array[String]): Unit = {

    val host = "0.0.0.0"
    val port = 8855

    val startupTask = for {
      bindings <- startServer(host, port).handleError
    } yield bindings

    startupTask.value.map {
      case Left(error) =>
        error.printStackTrace()
        system.terminate()
      case Right(_) =>
        println(s"Listening on port $port")
    }.onErrorRecover {
      case NonFatal(ex) =>
        ex.printStackTrace()
        system.terminate()
    }.runAsyncAndForget

  }
}

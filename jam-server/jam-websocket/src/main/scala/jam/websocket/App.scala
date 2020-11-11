package jam.websocket

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{Broadcast, Sink, Source}
import monix.eval.Task
import monix.execution.Scheduler

import jam.application.dsl.Result.Result
import jam.domains.Id
import jam.infrastructure.IdGenerator
import jam.infrastructure.interpreters.Log4jInterpreter
import jam.websocket.actors.{Disconnect, ServerActor}
import jam.websocket.dsl.UserDSL
import jam.websocket.interpreters.{InMemoryStoreInterpreter, InterpreterAsync}
import jam.websocket.messages.UserMessage
import jam.websocket.models.User
import jam.websocket.server.{ConnectionHandler, Server}

import scala.util.control.NonFatal
object App {

  implicit val system = ActorSystem()
  implicit val exc    = Scheduler.Implicits.global

  val actor           = system.actorOf(Props(classOf[ServerActor]))
  val MAX_BUFFER_SIZE = 16134

  private val messageActorSource =
    Source.queue[UserMessage](MAX_BUFFER_SIZE, OverflowStrategy.fail)

  type TaskResult[A] = Result[Task, AppError, A]

  def main(args: Array[String]): Unit = {
    import jam.application.dsl.syntax._
    import jam.application.shared.DeferEffectInstance._

    val logDSL      = new Log4jInterpreter[Task]
    val userDSL     = new UserDSL[Task](new InMemoryStoreInterpreter, logDSL)
    val interpreter = new InterpreterAsync().interpreter(userDSL)
    val disconnectSink = (userId: Id[User]) =>
      Sink.combine(
        Sink.actorRef[UserMessage](actor, 0, PartialFunction.empty),
        Sink.onComplete[UserMessage] { _ =>
          userDSL.deleteUser(userId).onErrorRestart(10).runToFuture

          actor ! Disconnect(userId)
        }
      )(Broadcast[UserMessage](_))
    val connectionHandler = ConnectionHandler(
      messageSource = messageActorSource,
      idGenerator = IdGenerator.createGenerator(),
      messageServer = ServerActor.flow(actor),
      messageInterpreter = interpreter,
      disconnectSink = disconnectSink
    )
    val interface = "0.0.0.0"
    val port      = 8866
    val startupTask = for {
      binding <- startServer(interface, port, connectionHandler).handleError
    } yield binding

    startupTask.value
      .map {
        case Right(_) =>
          println(s"Listning on port $interface:$port")
        case Left(error) =>
          println(error)
          system.terminate()
      }
      .onErrorRecover {
        case NonFatal(ex) =>
          ex.printStackTrace()
          system.terminate()
      }
      .runToFuture
  }

  private def startServer(
      interface: String,
      port: Int,
      handler: ConnectionHandler
  ): TaskResult[Http.ServerBinding] =
    Task.deferFuture {
      Server.start(interface, port, handler).map(Right(_)).recover {
        case NonFatal(ex) =>
          Left(InternalError(ex): AppError)
      }
    }

}

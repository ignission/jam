package jam.websocket.interpreters

import akka.NotUsed
import akka.stream.scaladsl.Flow
import cats.Applicative
import monix.eval.Task
import monix.execution.Scheduler

import jam.application.dsl.Result.Result
import jam.domains.{Id, User}
import jam.websocket.AppError
import jam.websocket.actors.NewClient
import jam.websocket.dsl.{InternalError, UserDSL}
import jam.websocket.messages.{ErrorOccured, NoReply, UnknownMessage, UserConnected, UserInfo, UserMessage}
import jam.websocket.server.Reply

import scala.concurrent.Future

trait Interpreter[F[_]] {
  def interpreter(userDSL: UserDSL[F]): Flow[UserMessage, Reply, NotUsed]
}

trait DSLExecution {
  def execute[F[_]: Applicative, A](userId: Id[User], result: => Result[F, AppError, A])(
      f: A => Reply
  ): F[Reply] = {
    val applicativeInstance = implicitly[Applicative[F]]
    applicativeInstance.map(result) {
      case Right(a) => f(a)
      case Left(error: AppError) =>
        Reply(ErrorOccured(userId, error.code))
      case Left(_) =>
        Reply(ErrorOccured(userId, InternalError))
    }
  }
}

class InterpreterAsync(implicit s: Scheduler) extends Interpreter[Task] with DSLExecution {
  override def interpreter(userDSL: UserDSL[Task]): Flow[UserMessage, Reply, NotUsed] =
    Flow[UserMessage].mapAsync(1) {
      case msg: UnknownMessage =>
        Future.successful(Reply(msg))
      case UserConnected(userId, name, queue) =>
        execute(userId, userDSL.addUser(User(userId, name))) { user =>
          Reply(UserInfo(userId, user), Seq(NewClient(userId, queue)))
        }.runToFuture
      case _ =>
        Future.successful(Reply(NoReply))
    }
}

package jam.websocket.interpreters

import akka.NotUsed
import akka.stream.scaladsl.Flow
import cats.Applicative

import jam.application.dsl.Result.Result
import jam.domains.{Id, User}
import jam.websocket.AppError
import jam.websocket.dsl.InternalError
import jam.websocket.messages.{ErrorOccured, UserMessage}
import jam.websocket.server.Reply

trait Interpreter[F[_]] {
  def interpreter(): Flow[UserMessage, Reply, NotUsed]
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

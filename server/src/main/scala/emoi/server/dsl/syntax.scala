package emoi.server.dsl

import cats.{Applicative, Monad}
import cats.data.EitherT
import cats.implicits._

object syntax {
  implicit class ResultMonadOps[F[_]: Monad, A](result: F[Either[AppError, A]]) {
    def handleError: EitherT[F, AppError, A] =
      EitherT(result)
  }
}

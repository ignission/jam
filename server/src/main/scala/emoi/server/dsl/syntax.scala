package emoi.server.dsl

import cats.Monad
import cats.data.EitherT

object syntax {
  implicit class ResultMonadOps[F[_]: Monad, A](result: F[Either[AppError, A]]) {
    def handleError: EitherT[F, AppError, A] =
      EitherT(result)
  }
}

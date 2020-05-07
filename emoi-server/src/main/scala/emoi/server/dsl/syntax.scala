package emoi.server.dsl

import cats.Monad
import cats.data.EitherT

object syntax {
  implicit class ResultMonadOps[F[_]: Monad, E, A](result: F[Either[E, A]]) {
    def handleError: EitherT[F, E, A] =
      EitherT(result)
  }
}

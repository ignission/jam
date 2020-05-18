package jam.dsl

import cats.Monad
import cats.data.EitherT
import cats.implicits._

object syntax {
  implicit class ResultMonadOps[F[_]: Monad, E, A](result: F[Either[E, A]]) {
    def handleError: EitherT[F, E, A] =
      EitherT(result)

    def mapError[E2](f: E => E2): F[Either[E2, A]] =
      result.map { inner =>
        inner.fold(
          error => Left(f(error)),
          data => Right(data)
        )
      }
  }
}

package jam.application.dsl

import cats.Applicative

object Result {
  type Result[F[_], E, A] = F[Either[E, A]]

  def success[F[_]: Applicative, E, A](a: A): Result[F, E, A] = {
    val applyInstance = implicitly[Applicative[F]]
    applyInstance.pure(Right(a))
  }

  def error[F[_]: Applicative, E, A](error: E): Result[F, E, A] = {
    val applyInstance = implicitly[Applicative[F]]
    applyInstance.pure(Left(error))
  }

}

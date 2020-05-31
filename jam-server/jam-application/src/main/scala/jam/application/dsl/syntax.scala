package jam.application.dsl

import cats.data.EitherT
import cats.implicits._
import cats.{Applicative, Monad}

object syntax {
  implicit class ResultOps[F[_]: Monad, E, A](result: F[Either[E, A]]) {
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

  implicit class ResultBooleanOps[F[_]: Monad](result: F[Boolean]) {
    def orError[E](error: E): F[Either[E, Unit]] =
      result.map {
        case false => Left[E, Unit](error)
        case true  => Right[E, Unit](())
      }
  }

  implicit class EitherOps[F[_]: Monad, E, A](result: Either[E, A]) {
    def lift: F[Either[E, A]] =
      Applicative[F].pure(result)

    def orFail: A =
      result match {
        case Right(value) => value
        case Left(error)  => throw new RuntimeException(error.toString)
      }
  }

  implicit class OptionOps[F[_]: Monad, A](optFValue: Option[F[A]]) {
    def sequence: F[Option[A]] =
      optFValue match {
        case Some(task) => task.map(Some(_))
        case None       => Applicative[F].pure(None)
      }

    // def toEither[E](error: E): F[Either[E, A]] =
    //   optFValue match {
    //     case Some(v: F[A]) => v.map(Right(_))
    //     case None          => Applicative[F].pure(Left(error))
    //   }
  }
}

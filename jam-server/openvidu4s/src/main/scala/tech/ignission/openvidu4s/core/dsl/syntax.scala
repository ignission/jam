package tech.ignission.openvidu4s.core.dsl

import cats.Monad
import cats.data.EitherT

import tech.ignission.openvidu4s.core.dsl.OpenViduHttpDsl.Response

object syntax {
  implicit class ResponseOps[F[_], A](response: F[Response[A]])(implicit M: Monad[F]) {
    def handleError: EitherT[F, HttpError, A] = EitherT(response)
  }
}

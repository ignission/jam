package tech.ignission.openvidu4s.core.dsl

import spray.json.JsonFormat
import OpenViduHttpDsl.Response

trait HttpDSL[F[_]] {
  def get[A](query: HttpQuery)(implicit format: JsonFormat[A]): F[Response[A]]
}

object OpenViduHttpDsl {
  type Response[A] = Either[HttpError, A]
}

sealed trait HttpError
case class RequestError(msg: String) extends HttpError
case class InvalidResponse(msg: String) extends HttpError
case object ServerDown                  extends HttpError

package emoi.server.dsl

import tech.ignission.openvidu4s.core.dsl.HttpError

sealed trait AppError extends RuntimeException

case class OpenViduClientError(error: HttpError) extends AppError
case class InternalError(cause: Throwable)       extends AppError

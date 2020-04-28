package emoi.server.dsl

sealed trait AppError extends RuntimeException

case class InternalError(cause: Throwable) extends AppError

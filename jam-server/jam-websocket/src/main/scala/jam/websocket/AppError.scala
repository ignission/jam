package jam.websocket

import jam.websocket.dsl.{ErrorCodes, InternalError}

sealed trait AppError {
  val code: ErrorCodes
}
case class LoggingFailed(ex: Throwable) extends AppError {
  val code: ErrorCodes = InternalError
}
case class NickNameAlreadyTaken(name: String) extends AppError {
  val code: ErrorCodes = InternalError
}

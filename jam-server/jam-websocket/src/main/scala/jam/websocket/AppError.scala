package jam.websocket

import jam.websocket.dsl.ErrorCodes

import scala.reflect.ClassTag

sealed trait AppError extends RuntimeException {
  val code: ErrorCodes
}

case class LoggingFailed(ex: Throwable) extends AppError {
  val code: ErrorCodes = ErrorCodes.InternalError

  override def getMessage: String =
    s"Cannot log: ${ex.getMessage()}"
}

case class NickNameAlreadyTaken(name: String) extends AppError {
  val code: ErrorCodes = ErrorCodes.InternalError

  override def getMessage: String =
    s"Nickname: $name is already taken"
}

case class NotFound[Key, A](key: Key)(implicit classTag: ClassTag[A]) extends AppError {
  val code: ErrorCodes = ErrorCodes.NotFound

  override def getMessage: String =
    s"Cannot found object of type $classTag with key: $key"
}

case class InternalError(ex: Throwable) extends AppError {
  val code: ErrorCodes = ErrorCodes.InternalError

  override def getCause: Throwable = ex
}

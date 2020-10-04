package jam.websocket

sealed trait AppError
case class LoggingFailed(ex: Throwable)       extends AppError
case class NickNameAlreadyTaken(name: String) extends AppError

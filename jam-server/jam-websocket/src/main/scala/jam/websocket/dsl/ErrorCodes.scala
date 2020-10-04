package jam.websocket.dsl

sealed trait ErrorCodes {
  val code: Int
}

object ErrorCodes {
  case object NotFound extends ErrorCodes {
    val code = 1
  }
  case object NickNameAlreadyTaken extends ErrorCodes {
    val code = 2
  }
  case object InternalError extends ErrorCodes {
    val code = 3
  }
}

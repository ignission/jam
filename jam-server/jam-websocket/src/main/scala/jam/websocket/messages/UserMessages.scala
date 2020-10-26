package jam.websocket.messages

import akka.stream.scaladsl.SourceQueue

import jam.domains.Id
import jam.websocket.dsl.ErrorCodes
import jam.websocket.models.{User, UserPosition}

sealed trait UserMessage {
  val userId: Id[User]
}

case class UserConnected(userId: Id[User], name: String, queue: SourceQueue[UserMessage])
    extends UserMessage
case object NoReply extends UserMessage {
  val userId: Id[User] = Id(0)
}
case class UserInfo(userId: Id[User], user: User)              extends UserMessage
case class UserMoved(userId: Id[User], position: UserPosition) extends UserMessage
case class UpdateUser(userId: Id[User], user: User)            extends UserMessage
case class ErrorOccured(userId: Id[User], code: ErrorCodes)    extends UserMessage
case class UnknownMessage(userId: Id[User], content: String)   extends UserMessage
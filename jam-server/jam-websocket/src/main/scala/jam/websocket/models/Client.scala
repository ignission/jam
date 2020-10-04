package jam.websocket.models

import akka.stream.scaladsl.SourceQueue

import jam.domains.{Id, User}
import jam.websocket.messages.UserMessage

case class Client(id: Id[User], queue: SourceQueue[UserMessage])

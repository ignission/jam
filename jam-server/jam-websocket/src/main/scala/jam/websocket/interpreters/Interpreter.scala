package jam.websocket.interpreters

import akka.NotUsed
import akka.stream.scaladsl.Flow

import jam.websocket.messages.UserMessage
import jam.websocket.server.Reply

trait Interpreter[F[_]] {
  def interpreter(): Flow[UserMessage, Reply, NotUsed]
}

trait DSLExecution {}

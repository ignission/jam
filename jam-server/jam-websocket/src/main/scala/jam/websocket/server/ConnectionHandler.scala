package jam.websocket.server

import akka.NotUsed
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.stream.FlowShape
import akka.stream.scaladsl.{Flow, GraphDSL, Merge, Sink, Source, SourceQueueWithComplete}
import spray.json._

import jam.domains.Id
import jam.infrastructure.IdGenerator.IdGenerator
import jam.websocket.actors.ActionMessage
import jam.websocket.messages.{UnknownMessage, UserConnected, UserMessage}
import jam.websocket.models.User

import scala.util.Try

case class Reply(msg: UserMessage, actions: Seq[ActionMessage] = Seq())

class ConnectionHandler(
    messageSource: Source[UserMessage, SourceQueueWithComplete[UserMessage]],
    idGenerator: IdGenerator[User],
    messageServer: Flow[Reply, UserMessage, NotUsed],
    messageInterpreter: Flow[UserMessage, Reply, NotUsed],
    disconnectSink: Id[User] => Sink[UserMessage, NotUsed]
) {
  import jam.websocket.messages.SprayJsonFormats._

  def create(nickName: String): Flow[Message, Message, Any] =
    Flow.fromGraph(GraphDSL.create(messageSource) { implicit builder => messageSourceActor =>
      {
        import GraphDSL.Implicits._

        val userId = idGenerator()
        val clientSourceMat = builder.materializedValue.map { sourceQueue =>
          UserConnected(userId, nickName, sourceQueue)
        }.via(messageInterpreter)

        val merge = builder.add(Merge[Reply](2))
        val inputMessage = builder.add(
          Flow[Message].collect {
            case TextMessage.Strict(text) =>
              Try(text.parseJson.convertTo[UserMessage]).getOrElse(UnknownMessage(userId, text))
          }
        )
        val replyMessages = builder.add(
          Flow[UserMessage].collect {
            case reply =>
              TextMessage(reply.toJson.prettyPrint)
          }
        )

        clientSourceMat ~> merge
        inputMessage ~> messageInterpreter ~> merge ~> messageServer ~> disconnectSink(userId)
        messageSourceActor ~> replyMessages
        FlowShape(inputMessage.in, replyMessages.out)

      }
    })
}

object ConnectionHandler {
  def apply(
      messageSource: Source[UserMessage, SourceQueueWithComplete[UserMessage]],
      idGenerator: IdGenerator[User],
      messageServer: Flow[Reply, UserMessage, NotUsed],
      messageInterpreter: Flow[UserMessage, Reply, NotUsed],
      disconnectSink: Id[User] => Sink[UserMessage, NotUsed]
  ): ConnectionHandler =
    new ConnectionHandler(
      messageSource,
      idGenerator,
      messageServer,
      messageInterpreter,
      disconnectSink
    )
}

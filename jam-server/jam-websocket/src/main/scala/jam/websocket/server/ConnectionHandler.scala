package jam.websocket.server

import jam.websocket.actors.ActionMessage
import jam.websocket.messages.UserMessage
// import jam.websocket.messages.{UserConnected, UserMessage}
// import akka.http.scaladsl.model.ws.Message
// import jam.infrastructure.IdGenerator

case class Reply(msg: UserMessage, actions: Seq[ActionMessage] = Seq())

// class ConnectionHandler(
//     messageSource: Source[UserMessage, SourceQueueWithComplete[UserMessage]],
//     idGenerator: IdGenerator[User],
//     messageServer: Flow[Reply, UserMessage, NotUsed]
// ) {

//   def create(nickName: String): Flow[Message, Message, Any] =
//     Flow.fromGraph(GraphDSL.create(messageSource)(_)) { implicit builder => messageSourceActor =>
//       {
//         import GraphDSL.Implicits._

//         val userId = idGenerator()
//         val clientSourceMat = builder.materializedValue.map { sourceQueue =>
//           UserConnected(userId, nickName, sourceQueue)
//         }.via(???)

//       }
//     }
// }

package actors

import akka.actor._
import play.api.libs.json.{JsValue, Json}

class WebSocketActor(clientActorRef: ActorRef, userName: String) extends Actor {
  private val logger = play.api.Logger(getClass)

  logger.info("Connecting user name: " + userName)

  def receive = {
    case jsValue: JsValue =>
      logger.info(s"Receive a message from $userName: ${jsValue.toString()}")
      val clientMessage = getMessage(jsValue)
      val json: JsValue = Json.parse(s"""{"body": "You said, ‘$clientMessage’"}""")
      clientActorRef ! json
  }

  def getMessage(json: JsValue): String =
    (json \ "message").as[String]
}

object WebSocketActor {
  def props(clientActorRef: ActorRef, userName: String): Props =
    Props(new WebSocketActor(clientActorRef, userName))
}

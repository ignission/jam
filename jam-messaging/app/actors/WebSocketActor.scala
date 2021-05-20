package actors

import akka.actor._
import play.api.libs.json.{JsValue, Json}

case class Position(x: Int, y: Int)

sealed trait Command
case class UserMove(position: Position) extends Command
case object Unknown                     extends Command

class WebSocketActor(clientActorRef: ActorRef, userName: String) extends Actor {
  private val logger = play.api.Logger(getClass)

  logger.info("Connecting user name: " + userName)

  def receive = {
    case jsValue: JsValue =>
      logger.info(s"Receive a message from $userName: ${jsValue.toString()}")
      val command       = parseCommand(jsValue)
      val json: JsValue = Json.parse(s"""{"body": "You said, ‘$command’"}""")
      clientActorRef ! json
  }

  def parseCommand(json: JsValue): Command =
    (json \ "command").as[String] match {
      case "move" =>
        UserMove(
          Position(
            x = (json \ "value" \ "x").as[Int],
            y = (json \ "value" \ "y").as[Int]
          )
        )
      case _ => Unknown
    }
}

object WebSocketActor {
  def props(clientActorRef: ActorRef, userName: String): Props =
    Props(new WebSocketActor(clientActorRef, userName))
}

package actors

import akka.actor.{Actor, ActorRef, Props}
import infrastructure.RedisClient
import play.api.libs.json._

case class User(name: String, position: Position)
case class Position(x: Int, y: Int)

sealed trait Command
case class UserMove(position: Position) extends Command
case object Unknown                     extends Command

class WebSocketActor(clientActorRef: ActorRef, redis: RedisClient, room: String, userName: String)
    extends Actor {
  private val logger = play.api.Logger(getClass)
  private val user   = User(userName, Position(0, 0))

  logger.info("Connecting user name: " + userName)

  implicit val positionFormat: Format[Position] = Json.format[Position]
  implicit val userFormat: Format[User]         = Json.format[User]

  redis.put(userName, Json.toJson(user).toString())

  def receive = {
    case jsValue: JsValue =>
      logger.info(s"Receive a message from $userName: ${jsValue.toString()}")
      val command = parseCommand(jsValue)

      val users = command match {
        case UserMove(position) =>
          val user = redis
            .get(userName)
            .map(Json.parse)
            .map(_.as[User])
            .getOrElse(throw new RuntimeException("user not found"))
          val updated = user.copy(position = position)

          redis.put(userName, Json.toJson(updated).toString())
          redis.getAll.map(Json.parse).map(_.as[User]).filterNot(_ == user) :+ updated
        case others => throw new RuntimeException("unknown command: " + others)
      }
      val json: JsValue = Json.obj(
        "users" -> Json.toJson(users)
      )
      logger.info("sending: " + json.toString())
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

  override def postStop(): Unit =
    redis.delete(userName)
}

object WebSocketActor {
  def props(clientActorRef: ActorRef, redis: RedisClient, room: String, userName: String): Props =
    Props(new WebSocketActor(clientActorRef, redis, room, userName))
}

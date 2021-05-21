package actors

import akka.actor.{Actor, ActorRef, Props}
import domain.models.{Position, Room, RoomName, User, UserCommand, UserName}
import infrastructure.RedisClient
import play.api.libs.json.{JsValue, Json}

class RoomRequestActor(out: ActorRef, redis: RedisClient, roomName: RoomName, userName: UserName)
    extends Actor {
  import formatters.PlayJsonFormats._
  import UserCommand._

  override def receive: Receive = {
    case msg: JsValue =>
      val request = msg.as[Move]
      out ! Move(request.userName, request.position)
  }

  override def preStart(): Unit = {
    val user = User(userName, Position(0, 0))
    val allUsers = redis.getAll.map(Json.parse).map(_.as[User])
    redis.put(userName.value, Json.toJson(user).toString())
    out ! Join(Room(roomName, (allUsers :+ user).toSet), user)
  }

  override def postStop(): Unit = {
    redis.delete(userName.value)
//    out ! Leave(userName)
//    out ! PoisonPill
  }
}

object RoomRequestActor {
  def props(
      out: ActorRef,
      redisClient: RedisClient,
      roomName: RoomName,
      userName: UserName
  ): Props = Props(
    new RoomRequestActor(out, redisClient, roomName, userName)
  )
}

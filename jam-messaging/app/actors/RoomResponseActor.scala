package actors

import akka.actor.{Actor, ActorRef, Props}
import domain.models.{User, UserName}
import infrastructure.RedisClient
import play.api.libs.json.Json

class RoomResponseActor(out: ActorRef, redisClient: RedisClient, myself: UserName) extends Actor {
  import formatters.PlayJsonFormats._
  import domain.models.UserCommand._

  override def receive: Receive = {
    case v: Move =>
      redisClient.put(myself.value, Json.toJson(User(v.userName, v.position)).toString())
      out ! Json.toJson(v)
    case v: Join =>
      out ! Json.toJson(v)
    case v: Leave =>
      redisClient.delete(v.userName.value)
      out ! Json.toJson(v)
      if (v.userName == myself) {
//        out ! PoisonPill
//        self ! PoisonPill
      }
  }

  override def postStop(): Unit =
    super.postStop()
}

object RoomResponseActor {
  def props(out: ActorRef, redisClient: RedisClient, myself: UserName): Props = Props(
    new RoomResponseActor(out, redisClient, myself)
  )
}

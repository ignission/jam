package actors

import akka.actor.{Actor, ActorRef, PoisonPill, Props}
import domain.models.UserName
import play.api.libs.json.Json

class RoomResponseActor(out: ActorRef, myself: UserName) extends Actor {
  import formatters.PlayJsonFormats._
  import domain.models.UserCommand._

  override def receive: Receive = {
    case v: Move =>
      out ! Json.toJson(v)
    case v: Join =>
      out ! Json.toJson(v)
    case v: Leave =>
      out ! Json.toJson(v)
      if (v.userName == myself) {
        out ! PoisonPill
        self ! PoisonPill
      }
  }

  override def postStop(): Unit =
    super.postStop()
}

object RoomResponseActor {
  def props(out: ActorRef, myself: UserName): Props = Props(new RoomResponseActor(out, myself))
}

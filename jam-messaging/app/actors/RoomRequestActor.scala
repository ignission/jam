package actors

import akka.actor.{Actor, ActorRef, PoisonPill, Props}
import domain.models.{UserCommand, UserName}
import play.api.libs.json.JsValue

class RoomRequestActor(out: ActorRef, userName: UserName) extends Actor {
  import formatters.PlayJsonFormats._
  import UserCommand._

  override def receive: Receive = {
    case msg: JsValue =>
      val request = msg.as[Move]
      out ! Move(request.userName, request.position)
  }

  override def preStart(): Unit =
    out ! Join(userName)

  override def postStop(): Unit = {
    out ! Leave(userName)
    out ! PoisonPill
  }
}

object RoomRequestActor {
  def props(out: ActorRef, userName: UserName): Props = Props(new RoomRequestActor(out, userName))
}

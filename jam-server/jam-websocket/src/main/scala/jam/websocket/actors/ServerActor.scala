package jam.websocket.actors

import akka.NotUsed
import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.stream.scaladsl.{Flow, SourceQueue}

import jam.domains.{Id, User}
import jam.websocket.messages.UserMessage
import jam.websocket.models.Client
import jam.websocket.server.Reply

import scala.collection.immutable.HashMap

sealed trait ActionMessage
case class NewClient(userId: Id[User], sourceQueue: SourceQueue[UserMessage]) extends ActionMessage
case class Disconnect(userId: Id[User])                                       extends ActionMessage
case class BroadcastIn(msg: UserMessage)                                      extends ActionMessage
case class Unicast(msg: UserMessage)                                          extends ActionMessage

class ServerActor extends Actor with ActorLogging {

  private var clients: HashMap[Id[User], Client] = HashMap.empty[Id[User], Client]

  override def receive: Actor.Receive = {
    case NewClient(userId, sourceQueue) =>
      log.debug(s"Adding new client ${userId.value}")
      clients = clients + (userId -> Client(userId, sourceQueue))
    case Disconnect(userId) =>
      log.debug(s"Disconnect ${userId.value}")
    case BroadcastIn(msg) =>
      log.debug(s"Broadcast message $msg")
      clients.foreach {
        case (_, client) =>
          client.queue.offer(msg)
        case _ =>
          ()
      }
    case Unicast(msg) =>
      log.debug(s"Unicast message $msg in ${msg.userId.value}")
      clients.get(msg.userId).foreach(_.queue.offer(msg))
  }
}

object ServerActor {
  def flow(actor: ActorRef): Flow[Reply, UserMessage, NotUsed] =
    Flow[Reply].map { reply =>
      reply.actions.foreach(action => actor ! action)
      actor ! Unicast(reply.msg)
      reply.msg
    }
}

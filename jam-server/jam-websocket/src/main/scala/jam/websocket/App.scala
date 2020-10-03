package jam.websocket

import akka.actor.ActorSystem
import monix.execution.Scheduler

object App {

  implicit val system = ActorSystem()
  implicit val exc    = Scheduler.Implicits.global

  def main(args: Array[String]): Unit = {}
}

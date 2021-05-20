package controllers

import actors.WebSocketActor
import akka.actor.ActorSystem
import play.api.libs.json.JsValue
import play.api.libs.streams.ActorFlow

import javax.inject._
import play.api.mvc._

@Singleton
class WebSocketsController @Inject() (val controllerComponents: ControllerComponents)(implicit
    system: ActorSystem
) extends BaseController {

//  private val logger = play.api.Logger(getClass)

  def ws(userName: String): WebSocket = WebSocket.accept[JsValue, JsValue] { _ =>
    ActorFlow.actorRef { actorRef =>
      WebSocketActor.props(actorRef, userName)
    }
  }
}

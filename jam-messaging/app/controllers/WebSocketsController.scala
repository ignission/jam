package controllers

import javax.inject._

import actors.{RoomRequestActor, RoomResponseActor}
import akka.actor.ActorSystem
import akka.stream.scaladsl._
import domain.models.{RoomName, UserCommand, UserName}
import play.api.libs.json.JsValue
import play.api.libs.streams.ActorFlow
import play.api.mvc._
import services.RoomService

@Singleton
class WebSocketsController @Inject() (
    val controllerComponents: ControllerComponents,
    service: RoomService
)(implicit
    system: ActorSystem
) extends BaseController {

  private val logger = play.api.Logger(getClass)

  def ws(roomName: String): WebSocket = WebSocket.accept[JsValue, JsValue] { implicit request =>
    logger.info("Room request: " + roomName)

    val userName =
      request.queryString("user_name").headOption.map(UserName(_)).getOrElse(UserName.anonymous)

    val userInput: Flow[JsValue, UserCommand, _] =
      ActorFlow.actorRef[JsValue, UserCommand](out => RoomRequestActor.props(out, userName))
    val roomInfo = service.start(roomName = RoomName(roomName), userName = userName)
    val userOutPut: Flow[UserCommand, JsValue, _] =
      ActorFlow.actorRef[UserCommand, JsValue](out => RoomResponseActor.props(out, userName))

    userInput.viaMat(roomInfo.bus)(Keep.right).viaMat(userOutPut)(Keep.right)
  }
}

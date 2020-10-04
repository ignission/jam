package jam.websocket.messages

import spray.json.DefaultJsonProtocol._
import spray.json._

import jam.websocket.dsl.ErrorCodes
import jam.websocket.messages._
import jam.websocket.models.{User, UserPosition}

object SprayJsonFormats {
  import jam.application.formatters.SprayJsonFormats._

  implicit val idUserFormat = idFormat[User]()

  implicit val userPositionFormat = jsonFormat2(UserPosition)
  implicit val userFormat         = jsonFormat3(User.apply)

  implicit val ErrorCodesFormat = new RootJsonFormat[ErrorCodes] {
    override def read(json: JsValue): ErrorCodes = ???
    override def write(obj: ErrorCodes): JsValue = ???
  }

  implicit val userInfoFormat       = jsonFormat2(UserInfo)
  implicit val userMovedFormat      = jsonFormat2(UserMoved)
  implicit val updateUserFormat     = jsonFormat2(UpdateUser)
  implicit val errorOccuredFormat   = jsonFormat2(ErrorOccured)
  implicit val unknownMessageFormat = jsonFormat2(UnknownMessage)

  implicit val userMessageFormat = new JsonFormat[UserMessage] {
    private final val USER_INFO_ID       = 1
    private final val USER_MOVED_ID      = 2;
    private final val UPDATE_USER_ID     = 3;
    private final val ERROR_OCCURED_ID   = 4;
    private final val UNKNOWN_MESSAGE_ID = 5;

    override def read(json: JsValue): UserMessage =
      json.asJsObject.getFields("type") match {
        case Seq(JsNumber(typeValue)) =>
          typeValue.toInt match {
            case USER_INFO_ID       => userInfoFormat.read(json)
            case USER_MOVED_ID      => userMovedFormat.read(json)
            case UPDATE_USER_ID     => updateUserFormat.read(json)
            case ERROR_OCCURED_ID   => errorOccuredFormat.read(json)
            case UNKNOWN_MESSAGE_ID => unknownMessageFormat.read(json)
          }
      }

    override def write(obj: UserMessage): JsValue = {
      val (typeValue, data) = obj match {
        case msg: UserInfo =>
          (USER_INFO_ID, userInfoFormat.write(msg))
        case msg: UserMoved =>
          (USER_MOVED_ID, userMovedFormat.write(msg))
        case msg: UpdateUser =>
          (UPDATE_USER_ID, updateUserFormat.write(msg))
        case msg: ErrorOccured =>
          (ERROR_OCCURED_ID, errorOccuredFormat.write(msg))
        case msg: UnknownMessage =>
          (UNKNOWN_MESSAGE_ID, unknownMessageFormat.write(msg))
        case _ =>
          deserializationError(s"Cannot send an internal message or request to clients")
      }
      val header = JsObject(
        "type" -> JsNumber(typeValue)
      )

      header.copy(fields = header.fields ++ data.asJsObject.fields)
    }
  }
}

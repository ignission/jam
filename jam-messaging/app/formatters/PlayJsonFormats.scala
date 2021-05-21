package formatters

import domain.models.{Position, UserCommand, UserName}
import play.api.libs.functional.syntax._
import play.api.libs.json._

object PlayJsonFormats {
  import UserCommand._

  implicit val positionReads: Reads[Position] = (
    (JsPath \ "x").read[Int] and
      (JsPath \ "y").read[Int]
  )(Position)

  implicit val moveCommandReads: Reads[Move] = (
    (JsPath \ "userName").read[String].map(UserName(_)) and
      (JsPath \ "position").read[Position]
  )(Move)

  implicit val positionWrites: Writes[Position] =
    (o: Position) =>
      Json.obj(
        "x" -> o.x,
        "y" -> o.y
      )

  implicit val moveCommandWrites: Writes[Move] =
    (o: Move) =>
      Json.obj(
        "command" -> "move",
        "user" -> Json.obj(
          "name"     -> o.userName.value,
          "position" -> o.position
        )
      )

  implicit val joinCommandWrites: Writes[Join] =
    (o: Join) =>
      Json.obj(
        "command"  -> "join",
        "userName" -> o.userName.value
      )

  implicit val leaveCommandWrites: Writes[Leave] =
    (o: Leave) =>
      Json.obj(
        "command"  -> "leave",
        "userName" -> o.userName.value
      )
}

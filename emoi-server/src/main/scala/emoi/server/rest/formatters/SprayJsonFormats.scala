package emoi.server.rest.formatters

import spray.json._
import tech.ignission.openvidu4s.core.datas.Session
import tech.ignission.openvidu4s.core.datas.GeneratedToken

object SprayJsonFormats extends DefaultJsonProtocol {
  import tech.ignission.openvidu4s.core.formatters.SprayJsonFormats._

  implicit object SessionsFormat extends RootJsonWriter[Seq[Session]] {
    override def write(obj: Seq[Session]): JsValue =
      JsObject(
        "sessions" ->
          obj.map { session =>
            JsObject(
              "sessionId" -> session.id.toJson,
              "createdAt" -> ZonedDateTimeFormat.write(session.createdAt)
            )
          }.toJson
      )
  }

  implicit object GeneratedTokenFormat extends RootJsonWriter[GeneratedToken] {
    override def write(obj: GeneratedToken): JsValue =
      JsObject(
        "token" -> obj.token.value.toJson
      )
  }
}

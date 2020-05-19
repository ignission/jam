package jam.rest.formatters

import spray.json._

import jam.rest.routes.CreateSessionRequest

import tech.ignission.openvidu4s.core.datas.{GeneratedToken, InitializedSession, Session}

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

  implicit object InitializedSessionFormat extends RootJsonWriter[InitializedSession] {
    override def write(obj: InitializedSession): JsValue =
      JsObject(
        "session" -> JsObject(
          "id"        -> obj.id.toJson,
          "createdAt" -> ZonedDateTimeFormat.write(obj.createdAt)
        )
      )
  }

  implicit object GeneratedTokenFormat extends RootJsonWriter[GeneratedToken] {
    override def write(obj: GeneratedToken): JsValue =
      JsObject(
        "token" -> obj.token.value.toJson
      )
  }

  implicit val createSessionRequestFormat = jsonFormat1(CreateSessionRequest)
}

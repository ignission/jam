package jam.rest.formatters

import spray.json._

import jam.application.accounts.SignUpRequest
import jam.domains.Id
import jam.domains.auth.{Account, Password}
import jam.rest.routes.CreateSessionRequest

import tech.ignission.openvidu4s.core.datas.{GeneratedToken, InitializedSession, Session}

object SprayJsonFormats extends DefaultJsonProtocol {
  import tech.ignission.openvidu4s.core.formatters.SprayJsonFormats._

  class IdFormat[A]() extends RootJsonFormat[Id[A]] {
    override def read(json: JsValue): Id[A] =
      json match {
        case JsNumber(idVal) => Id[A](idVal.toLong)
        case _ =>
          throw DeserializationException(s"Expected a js number got ${json.prettyPrint}")
      }
    override def write(obj: Id[A]): JsValue =
      JsNumber(obj.value)
  }

  implicit val accountIdFormat = new IdFormat[Account]

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

  implicit object PasswordFormat extends RootJsonReader[Password] {
    override def read(json: JsValue): Password =
      json match {
        case JsString(value) =>
          Password(value)
        case _ =>
          throw DeserializationException(
            s"Expected a string. Input: ${json.prettyPrint}"
          )
      }
  }

  implicit val createSessionRequestFormat = jsonFormat1(CreateSessionRequest)
  implicit val signUpRequestFormat        = jsonFormat4(SignUpRequest)
}

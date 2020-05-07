package tech.ignission.openvidu4s.core.formatters

import spray.json._
import tech.ignission.openvidu4s.core.datas.InitializeSession
import tech.ignission.openvidu4s.core.datas.SessionId
import tech.ignission.openvidu4s.core.datas.InitializedSession
import java.time.ZonedDateTime
import java.time.ZoneId
import java.time.Instant
import tech.ignission.openvidu4s.core.datas.Session
import tech.ignission.openvidu4s.core.datas.GenerateToken
import tech.ignission.openvidu4s.core.datas.Role
import tech.ignission.openvidu4s.core.datas.Role.MODERATOR
import tech.ignission.openvidu4s.core.datas.Role.PUBLISHER
import tech.ignission.openvidu4s.core.datas.Role.SUBSCRIBER
import tech.ignission.openvidu4s.core.datas.GeneratedToken
import tech.ignission.openvidu4s.core.datas.Token

object SprayJsonFormats extends DefaultJsonProtocol {

  implicit object ZonedDateTimeFormat extends RootJsonFormat[ZonedDateTime] {
    override def read(json: JsValue): ZonedDateTime =
      json match {
        case JsNumber(value) =>
          ZonedDateTime.ofInstant(Instant.ofEpochMilli(value.toLong), ZoneId.systemDefault())
        case _ =>
          throw DeserializationException(
            s"Expected an epoch milli. Input: ${json.prettyPrint}"
          )
      }
    override def write(obj: ZonedDateTime): JsValue =
      JsNumber(
        obj.toInstant.toEpochMilli
      )
  }

  implicit object SessionIdFormat extends RootJsonFormat[SessionId] {
    override def read(json: JsValue): SessionId =
      json match {
        case JsString(str) => SessionId(str)
        case _ =>
          throw DeserializationException(
            s"Expected a session id. Input: ${json.prettyPrint}"
          )
      }
    override def write(obj: SessionId): JsValue =
      JsString(obj.value)
  }

  implicit object InitializeSessionFormat extends RootJsonWriter[InitializeSession] {
    override def write(obj: InitializeSession): JsValue =
      JsObject(
        "customSessionId" -> obj.id.toJson
      )
  }

  implicit object InitializedSessionFormat extends RootJsonReader[InitializedSession] {
    override def read(json: JsValue): InitializedSession =
      json.asJsObject.getFields("id", "createdAt") match {
        case Seq(JsString(id), createdAt) =>
          InitializedSession(
            id = SessionId(id),
            createdAt = ZonedDateTimeFormat.read(createdAt)
          )
        case _ =>
          throw DeserializationException(
            s"Expected an initialized session. Input: ${json.prettyPrint}"
          )
      }
  }

  implicit object SessionsFormat extends RootJsonReader[Seq[Session]] {
    override def read(json: JsValue): Seq[Session] =
      json.asJsObject.getFields("content") match {
        case Seq(JsArray(contents)) =>
          contents.map { content =>
            content.asJsObject.getFields("sessionId", "createdAt") match {
              case Seq(JsString(sessionId), createdAt) =>
                Session(
                  id = SessionId(sessionId),
                  createdAt = ZonedDateTimeFormat.read(createdAt)
                )
              case _ =>
                throw DeserializationException(
                  s"Expected a session. Input: ${json.prettyPrint}"
                )
            }
          }
        case _ =>
          throw DeserializationException(
            s"Expected sessions. Input: ${json.prettyPrint}"
          )
      }
  }

  implicit object RoleFormat extends RootJsonFormat[Role] {
    override def read(json: JsValue): Role =
      json match {
        case JsString(value) =>
          value match {
            case "MODERATOR" =>
              Role.MODERATOR
            case "PUBLISHER" =>
              Role.PUBLISHER
            case "SUBSCRIBER" =>
              Role.SUBSCRIBER
            case _ =>
              throw DeserializationException(s"Expected role string. Input: ${value}")
          }
        case _ =>
          throw DeserializationException(
            s"Expected role. Input: ${json.prettyPrint}"
          )
      }
    override def write(obj: Role): JsValue =
      obj match {
        case MODERATOR  => JsString("MODERATOR")
        case PUBLISHER  => JsString("PUBLISHER")
        case SUBSCRIBER => JsString("SUBSCRIBER")
      }
  }

  implicit object GenerateTokenFormat extends RootJsonWriter[GenerateToken] {
    override def write(obj: GenerateToken): JsValue =
      JsObject(
        "session" -> obj.sessionId.value.toJson,
        "role"    -> obj.role.toJson
      )
  }

  implicit object GeneratedTokenFormat extends RootJsonReader[GeneratedToken] {
    override def read(json: JsValue): GeneratedToken =
      json.asJsObject.getFields("session", "role", "token") match {
        case Seq(JsString(session), JsString(role), JsString(token)) =>
          GeneratedToken(
            sessionId = SessionId(session),
            token = Token(token)
          )
        case _ =>
          throw DeserializationException(s"Expected token. Input: ${json.prettyPrint}")
      }
  }
}

package tech.ignission.openvidu4s.core.formatters

import spray.json._
import tech.ignission.openvidu4s.core.datas.InitializeSession
import tech.ignission.openvidu4s.core.datas.SessionId
import tech.ignission.openvidu4s.core.datas.InitializedSession
import java.time.ZonedDateTime
import java.time.ZoneId
import java.time.Instant
import tech.ignission.openvidu4s.core.datas.Session

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
        case Seq(JsString(id), JsNumber(createdAt)) =>
          InitializedSession(
            id = SessionId(id),
            createdAt = ZonedDateTimeFormat.read(JsNumber(createdAt))
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
}

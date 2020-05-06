package tech.ignission.openvidu4s.core.datas

import java.time.ZonedDateTime

case class SessionId(value: String) extends AnyVal

case class InitializeSession(id: SessionId)

case class InitializedSession(id: SessionId, createdAt: ZonedDateTime)

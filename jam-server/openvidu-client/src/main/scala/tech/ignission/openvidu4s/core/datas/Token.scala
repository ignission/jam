package tech.ignission.openvidu4s.core.datas

case class Token(value: String) extends AnyVal

case class GenerateToken(sessionId: SessionId, role: Role = Role.PUBLISHER)

case class GeneratedToken(sessionId: SessionId, token: Token)

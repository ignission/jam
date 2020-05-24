package jam.rest.routes

import tech.ignission.openvidu4s.core.datas.SessionId

case class CreateSessionRequest(sessionId: SessionId)

case class SignUpRequest(name: String, email: String, password: String)

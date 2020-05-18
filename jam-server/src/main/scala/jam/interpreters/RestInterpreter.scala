package jam.interpreters

import jam.dsl.{OpenViduClientError, RestDSL}
import jam.dsl.RestDSL.Result
import monix.eval.Task
import tech.ignission.openvidu4s.core.apis.AllAPI
import tech.ignission.openvidu4s.core.datas.{GenerateToken, GeneratedToken, Session, SessionId}

class RestInterpreter(openviduAPI: AllAPI[Task]) extends RestDSL[Task] {
  import jam.dsl.syntax._

  override def listSessions: Result[Task, Seq[Session]] =
    openviduAPI.sessionAPI.getSessions.mapError(OpenViduClientError)

  override def generateToken(sessionId: SessionId): Result[Task, GeneratedToken] =
    openviduAPI.tokenAPI.generateToken(GenerateToken(sessionId)).mapError(OpenViduClientError)
}

package jam.interpreters

import monix.eval.Task

import jam.dsl.RestDSL.Result
import jam.dsl.{OpenViduClientError, RestDSL}

import tech.ignission.openvidu4s.core.apis.AllAPI
import tech.ignission.openvidu4s.core.datas.{
  GenerateToken,
  GeneratedToken,
  InitializeSession,
  InitializedSession,
  Session,
  SessionId
}
import jam.rest.routes.SignUpRequest

class RestInterpreter(openviduAPI: AllAPI[Task]) extends RestDSL[Task] {

  import jam.dsl.syntax._

  override def listSessions: Result[Task, Seq[Session]] =
    openviduAPI.sessionAPI.getSessions.mapError(OpenViduClientError)

  override def generateToken(sessionId: SessionId): Result[Task, GeneratedToken] =
    openviduAPI.tokenAPI.generateToken(GenerateToken(sessionId)).mapError(OpenViduClientError)

  override def createSession(sessionId: SessionId): Result[Task, InitializedSession] =
    openviduAPI.sessionAPI
      .initializeSession(InitializeSession(sessionId))
      .mapError(OpenViduClientError)

  override def signUp(requst: SignUpRequest): RestDSL.Result[Task, Unit] = ???

}

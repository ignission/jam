package jam.application.sessions

import cats.Monad

import jam.application.OpenViduClientError

import tech.ignission.openvidu4s.core.apis.AllAPI
import tech.ignission.openvidu4s.core.datas.{
  GenerateToken,
  GeneratedToken,
  InitializeSession,
  InitializedSession,
  Session,
  SessionId
}

class SessionService[F[_]: Monad](openviduAPI: AllAPI[F]) {
  import jam.application.dsl.syntax._

  def listSessions: F[Either[OpenViduClientError, Seq[Session]]] =
    openviduAPI.sessionAPI.getSessions.mapError(OpenViduClientError)

  def generateToken(sessionId: SessionId): F[Either[OpenViduClientError, GeneratedToken]] =
    openviduAPI.tokenAPI.generateToken(GenerateToken(sessionId)).mapError(OpenViduClientError)

  def createSession(sessionId: SessionId): F[Either[OpenViduClientError, InitializedSession]] =
    openviduAPI.sessionAPI
      .initializeSession(InitializeSession(sessionId))
      .mapError(OpenViduClientError)
}

package jam.dsl

import jam.dsl.RestDSL.Result
import tech.ignission.openvidu4s.core.datas.{GeneratedToken, Session, SessionId, InitializedSession}

trait RestDSL[F[_]] {
  def listSessions: Result[F, Seq[Session]]
  def generateToken(sessionId: SessionId): Result[F, GeneratedToken]
  def createSession(sessionId: SessionId): Result[F, InitializedSession]
}

object RestDSL {
  type Result[F[_], A] = F[Either[AppError, A]]
}

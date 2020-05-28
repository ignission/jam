package jam.dsl

import cats.Monad

import jam.domains.Id
import jam.domains.auth.{Account, AccountRepository}
import jam.dsl.RestDSL.Result
import jam.rest.routes.SignUpRequest

import tech.ignission.openvidu4s.core.datas.{GeneratedToken, InitializedSession, Session, SessionId}

trait RestDSL[F[_]] {
  def listSessions: Result[F, Seq[Session]]
  def generateToken(sessionId: SessionId): Result[F, GeneratedToken]
  def createSession(sessionId: SessionId): Result[F, InitializedSession]
  def signUp(requst: SignUpRequest): Result[F, Unit]
}

object RestDSL {
  type Result[F[_], A] = F[Either[AppError, A]]
}

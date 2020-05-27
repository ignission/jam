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
  def signUp[G[_]: Monad: AccountRepository](requst: SignUpRequest): Result[F, Unit] = {
    AccountRepository[G].find(Id[Account](requst.email.toLong))
    ???
  }
}

object RestDSL {
  type Result[F[_], A] = F[Either[AppError, A]]
}

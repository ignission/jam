package jam.application.services

import jam.domain.models.{Session, Token}

trait WebRtcService {
  def createSession(id: String): Session
  def getToken(session: Session): Token
}

object WebRtcService {

//  def createSession(id: String): ZIO[WebRtcService, Nothing, Session] =
//    ZIO.accessM(env => IO.effectTotal(env.createSession(id)))
//
//  def getToken(session: Session): ZIO[WebRtcService, Nothing, Token] =
//    ZIO.accessM(env => IO.effectTotal(env.getToken(session)))
}

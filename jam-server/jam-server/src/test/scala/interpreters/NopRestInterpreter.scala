package interpreters

import java.time.{Instant, ZoneId, ZonedDateTime}

import monix.eval.Task

import jam.dsl.RestDSL
import jam.dsl.RestDSL.Result

import tech.ignission.openvidu4s.core.datas
import tech.ignission.openvidu4s.core.datas.{
  GeneratedToken,
  InitializedSession,
  Session,
  SessionId,
  Token
}

class NopRestInterpreter extends RestDSL[Task] {

  override def listSessions: Result[Task, Seq[Session]] =
    Task {
      Right(
        Seq(
          Session(
            id = datas.SessionId("test-session1"),
            ZonedDateTime.ofInstant(Instant.ofEpochMilli(1589035535985L), ZoneId.systemDefault())
          )
        )
      )
    }

  override def generateToken(sessionId: SessionId): RestDSL.Result[Task, GeneratedToken] =
    Task {
      Right(
        GeneratedToken(
          sessionId = sessionId,
          token = Token("abcdefg")
        )
      )
    }

  override def createSession(sessionId: SessionId): RestDSL.Result[Task, InitializedSession] =
    Task {
      Right(
        InitializedSession(
          id = sessionId,
          ZonedDateTime.ofInstant(Instant.ofEpochMilli(1589035535985L), ZoneId.systemDefault())
        )
      )
    }

}

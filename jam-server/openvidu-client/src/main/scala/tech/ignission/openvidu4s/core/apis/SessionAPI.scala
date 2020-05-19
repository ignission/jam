package tech.ignission.openvidu4s.core.apis

import tech.ignission.openvidu4s.core.Credentials
import tech.ignission.openvidu4s.core.datas.{InitializeSession, InitializedSession, Session, SessionId}
import tech.ignission.openvidu4s.core.dsl.OpenViduHttpDsl.Response
import tech.ignission.openvidu4s.core.dsl.{HttpDSL, HttpQuery}

class SessionAPI[F[_]](baseUrl: String, credentials: Credentials)(implicit httpDSL: HttpDSL[F]) {
  import tech.ignission.openvidu4s.core.formatters.SprayJsonFormats._

  private val resource = s"/api/sessions"

  lazy val getSessions: F[Response[Seq[Session]]] =
    httpDSL.get[Seq[Session]](
      HttpQuery(
        path = resource,
        credentials = credentials,
        baseUrl = baseUrl
      )
    )

  def initializeSession(initializeSession: InitializeSession): F[Response[InitializedSession]] =
    httpDSL.post[InitializeSession, InitializedSession](
      HttpQuery(
        path = resource,
        credentials = credentials,
        baseUrl = baseUrl
      ),
      initializeSession
    )

  def closeSession(sessionId: SessionId): F[Response[Unit]] =
    httpDSL.delete(
      HttpQuery(
        path = s"$resource/${sessionId.value}",
        credentials = credentials,
        baseUrl = baseUrl
      )
    )
}

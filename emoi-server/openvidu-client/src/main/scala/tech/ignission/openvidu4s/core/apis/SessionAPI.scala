package tech.ignission.openvidu4s.core.apis

import tech.ignission.openvidu4s.core.dsl.OpenViduHttpDsl.Response
import tech.ignission.openvidu4s.core.Credentials
import tech.ignission.openvidu4s.core.dsl.HttpDSL
import tech.ignission.openvidu4s.core.dsl.HttpQuery
import tech.ignission.openvidu4s.core.datas.InitializeSession
import tech.ignission.openvidu4s.core.datas.InitializedSession

class SessionAPI[F[_]](baseUrl: String, credentials: Credentials)(implicit httpDSL: HttpDSL[F]) {
  import tech.ignission.openvidu4s.core.formatters.SprayJsonFormats._

  private val resource = s"/api/sessions"

  def initializeSession(initializeSession: InitializeSession): F[Response[InitializedSession]] =
    httpDSL.post[InitializeSession, InitializedSession](
      HttpQuery(
        path = resource,
        credentials = credentials,
        baseUrl = baseUrl
      ),
      initializeSession
    )
}

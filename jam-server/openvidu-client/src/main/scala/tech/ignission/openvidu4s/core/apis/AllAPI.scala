package tech.ignission.openvidu4s.core.apis

import tech.ignission.openvidu4s.core.Credentials
import tech.ignission.openvidu4s.core.dsl.HttpDSL

class AllAPI[F[_]](baseUrl: String, credentials: Credentials)(implicit httpDsl: HttpDSL[F]) {

  lazy val sessionAPI = new SessionAPI(baseUrl, credentials)
  lazy val tokenAPI   = new TokenAPI(baseUrl, credentials)
}

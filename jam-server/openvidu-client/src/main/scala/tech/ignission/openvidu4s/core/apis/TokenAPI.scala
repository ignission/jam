package tech.ignission.openvidu4s.core.apis

import tech.ignission.openvidu4s.core.Credentials
import tech.ignission.openvidu4s.core.datas.{GenerateToken, GeneratedToken}
import tech.ignission.openvidu4s.core.dsl.OpenViduHttpDsl.Response
import tech.ignission.openvidu4s.core.dsl.{HttpDSL, HttpQuery}

class TokenAPI[F[_]](baseUrl: String, credentials: Credentials)(implicit httpDSL: HttpDSL[F]) {
  import tech.ignission.openvidu4s.core.formatters.SprayJsonFormats._

  private val resource = s"/api/tokens"

  def generateToken(generateToken: GenerateToken): F[Response[GeneratedToken]] =
    httpDSL.post[GenerateToken, GeneratedToken](
      HttpQuery(
        path = resource,
        credentials = credentials,
        baseUrl = baseUrl
      ),
      generateToken
    )
}

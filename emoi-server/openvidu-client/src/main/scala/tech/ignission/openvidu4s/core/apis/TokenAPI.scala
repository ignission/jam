package tech.ignission.openvidu4s.core.apis

import tech.ignission.openvidu4s.core.dsl.OpenViduHttpDsl.Response
import tech.ignission.openvidu4s.core.Credentials
import tech.ignission.openvidu4s.core.dsl.HttpDSL
import tech.ignission.openvidu4s.core.dsl.HttpQuery
import tech.ignission.openvidu4s.core.datas.GenerateToken
import tech.ignission.openvidu4s.core.datas.GeneratedToken

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

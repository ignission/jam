package jam.infrastructure.http

import io.circe._
import org.atnos.eff._

case class HttpParam(url: String, credential: Credential)

sealed trait Credential
case class BasicCredential(username: String, password: String) extends Credential

sealed trait HttpDSL[+A]
case class Get[A](param: HttpParam, decoder: Decoder[A]) extends HttpDSL[A]
case class Post[A1, A2](
    param: HttpParam,
    data: A1,
    encoder: Encoder[A1],
    decoder: Decoder[A2]
) extends HttpDSL[A2]

object HttpDSL {
  type _http[R] = HttpDSL |= R

  def get[A, R: _http](param: HttpParam)(implicit decoder: Decoder[A]): Eff[R, A] =
    Eff.send[HttpDSL, R, A](Get(param, decoder))

  def post[A1, A2, R: _http](param: HttpParam, data: A1)(implicit
      encoder: Encoder[A1],
      decoder: Decoder[A2]
  ): Eff[R, A2] =
    Eff.send[HttpDSL, R, A2](Post(param, data, encoder, decoder))
}

package jam.application.effects.http

import io.circe._
import org.atnos.eff._

sealed trait HttpEffect[+A]
case class Get[A](param: HttpParam, decoder: Decoder[A]) extends HttpEffect[A]
case class Post[A1, A2](
    param: HttpParam,
    data: A1,
    encoder: Encoder[A1],
    decoder: Decoder[A2]
) extends HttpEffect[A2]

object HttpEffect {
  type _http[R] = HttpEffect |= R

  def get[A, R: _http](param: HttpParam)(implicit decoder: Decoder[A]): Eff[R, A] =
    Eff.send[HttpEffect, R, A](Get(param, decoder))

  def post[A1, A2, R: _http](param: HttpParam, data: A1)(implicit
      encoder: Encoder[A1],
      decoder: Decoder[A2]
  ): Eff[R, A2] =
    Eff.send[HttpEffect, R, A2](Post(param, data, encoder, decoder))
}

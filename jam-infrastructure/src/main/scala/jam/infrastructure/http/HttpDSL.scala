package jam.infrastructure.http

import org.atnos.eff.Eff.send
import org.atnos.eff.Interpret.translate
import org.atnos.eff._
import org.atnos.eff.all.{_throwableEither, fromEither}
import sttp.client3._

case class HttpParam(url: String, credential: Credential)

sealed trait Credential
case class BasicCredential(username: String, password: String) extends Credential

sealed trait HttpDSL[+A]
case class Get[A](param: HttpParam) extends HttpDSL[A]

object HttpDSL {
  type Stack    = Fx.fx2[HttpDSL, Either[Throwable, *]]
  type _http[R] = HttpDSL |= R

  def get[A, R: _http](param: HttpParam): Eff[R, A] =
    Eff.send[HttpDSL, R, A](Get(param))
}


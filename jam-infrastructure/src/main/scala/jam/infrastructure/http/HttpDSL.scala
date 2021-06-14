package jam.infrastructure.http

import org.atnos.eff.Interpret.translate
import org.atnos.eff._
import org.atnos.eff.all.fromEither
import sttp.client3._

case class HttpParam(url: String, credential: Credential)

sealed trait Credential
case class BasicCredential(username: String, password: String) extends Credential

sealed trait HttpDSL[+A]
case class Get(param: HttpParam) extends HttpDSL[String]

object HttpDSL {
  type Stack    = Fx.fx2[HttpDSL, Either[String, *]]
  type _http[R] = HttpDSL |= R

  def get[A, R: _http](param: HttpParam): Eff[R, String] =
    Eff.send[HttpDSL, R, String](Get(param))
}

object HttpInterpreter {
  type _either[R] = Either[String, *] |= R

  def run[R, U, A](
      effects: Eff[R, A]
  )(implicit m: Member.Aux[HttpDSL, R, U], either: _either[U]): Eff[U, A] =
    translate(effects)(new Translate[HttpDSL, U] {
      private val backend = HttpURLConnectionBackend()

      override def apply[X](kv: HttpDSL[X]): Eff[U, X] =
        kv match {
          case Get(param) =>
            val res = buildGetRequest(param).send(backend)
            fromEither(res.body)
        }
    })

  private def buildGetRequest(param: HttpParam): RequestT[Identity, Either[String, String], Any] =
    buildRequest(param).get(uri"${param.url}").response(asString)

  private def buildRequest(param: HttpParam): RequestT[Empty, Either[String, String], Any] =
    param.credential match {
      case BasicCredential(username, password) =>
        basicRequest.auth.basic(user = username, password = password)
    }
}

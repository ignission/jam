package jam.infrastructure.http

import io.circe._
import org.atnos.eff.Interpret.translate
import org.atnos.eff._
import org.atnos.eff.all.fromEither
import sttp.client3._
import sttp.client3.circe._

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
  type Stack    = Fx.fx2[HttpDSL, Either[String, *]]
  type _http[R] = HttpDSL |= R

  def get[A, R: _http](param: HttpParam)(implicit decoder: Decoder[A]): Eff[R, A] =
    Eff.send[HttpDSL, R, A](Get(param, decoder))
}

object HttpInterpreter {
  type _either[R] = Either[ResponseException[String, Error], *] |= R

  def run[R, U, A](
      effects: Eff[R, A]
  )(implicit m: Member.Aux[HttpDSL, R, U], either: _either[U]): Eff[U, A] =
    translate(effects)(new Translate[HttpDSL, U] {
      private val backend = HttpURLConnectionBackend()

      override def apply[X](kv: HttpDSL[X]): Eff[U, X] = {
        val result = kv match {
          case Get(param, decoder) =>
            get(param)(decoder).send(backend).body
          case Post(param, data, encoder, decoder) =>
            post(param, data)(encoder, decoder).send(backend).body
        }
        fromEither(result)
      }

      private def get[T](
          param: HttpParam
      )(implicit
          decoder: Decoder[T]
      ): RequestT[Identity, Either[ResponseException[String, Error], T], Any] =
        setCredential(param).get(uri"${param.url}").response(asJson[T])

      private def post[T1, T2](param: HttpParam, data: T1)(implicit
          encoder: Encoder[T1],
          decoder: Decoder[T2]
      ): RequestT[Identity, Either[ResponseException[String, Error], T2], Any] =
        setCredential(param).post(uri"${param.url}").body(data).response(asJson[T2])

      private def setCredential(param: HttpParam): RequestT[Empty, Either[String, String], Any] =
        param.credential match {
          case BasicCredential(username, password) =>
            basicRequest.auth.basic(user = username, password = password)
        }
    })

}

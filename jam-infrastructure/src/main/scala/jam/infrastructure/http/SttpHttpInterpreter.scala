package jam.infrastructure.http

import io.circe._
import org.atnos.eff.Interpret.translate
import org.atnos.eff._
import org.atnos.eff.all.fromEither
import sttp.client3.circe._
import sttp.client3.{ResponseException, _}

import jam.application.effects.http._

object SttpHttpInterpreter {

  type _either[R] = Either[ResponseException[String, Error], *] |= R

  def run[R, U, A](
      effects: Eff[R, A]
  )(implicit m: Member.Aux[HttpEffect, R, U], either: _either[U]): Eff[U, A] =
    translate(effects)(new Translate[HttpEffect, U] {
      private val backend = HttpURLConnectionBackend()

      override def apply[X](kv: HttpEffect[X]): Eff[U, X] = {
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

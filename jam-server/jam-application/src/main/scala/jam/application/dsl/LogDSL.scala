package jam.application.dsl

import jam.application.dsl.Result.Result

trait LogDSL[F[_]] {
  def debug(msg: String): Result[F, Throwable, Unit]
  def trace(msg: String): Result[F, Throwable, Unit]
  def error(msg: String, optError: Option[Throwable]): Result[F, Throwable, Unit]
  def warn(msg: String): Result[F, Throwable, Unit]
}

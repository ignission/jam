package jam.infrastructure.interpreters

import org.apache.logging.log4j.LogManager

import jam.application.dsl.Result.Result
import jam.application.dsl.{LogDSL, Result}
import jam.application.shared.DeferEffect

import scala.util.Try
import scala.util.control.NonFatal

class Log4jInterpreter[F[_]: DeferEffect] extends LogDSL[F] {

  private val logger              = LogManager.getLogger()
  private val deferEffectInstance = implicitly[DeferEffect[F]]

  private def handleException(f: => Unit): Result[F, Throwable, Unit] =
    deferEffectInstance.deferEffect {
      Try(f)
        .map(Right(_))
        .recover {
          case NonFatal(ex) =>
            Left(ex)
        }
        .get
    }

  override def debug(msg: String): Result[F, Throwable, Unit] =
    handleException(logger.debug(msg))

  override def error(msg: String, optError: Option[Throwable]): Result[F, Throwable, Unit] =
    handleException(logger.error(msg, optError))

  override def trace(msg: String): Result.Result[F, Throwable, Unit] =
    handleException(logger.trace(msg))

  override def warn(msg: String): Result.Result[F, Throwable, Unit] =
    handleException(logger.warn(msg))
}

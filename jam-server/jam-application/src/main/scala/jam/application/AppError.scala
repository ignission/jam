package jam.application

import tech.ignission.openvidu4s.core.dsl.HttpError
import jam.domains.auth.AccountError

sealed trait AppError extends RuntimeException

case class OpenViduClientError(inner: HttpError)    extends AppError
case class InternalError(cause: Throwable)          extends AppError
case class AccountServiceError(inner: AccountError) extends AppError

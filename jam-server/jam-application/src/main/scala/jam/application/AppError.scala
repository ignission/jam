package jam.application

import jam.domains.auth.AccountError

import tech.ignission.openvidu4s.core.dsl.HttpError

sealed trait AppError extends RuntimeException

case class OpenViduClientError(inner: HttpError)    extends AppError
case class InternalError(cause: Throwable)          extends AppError
case class AccountServiceError(inner: AccountError) extends AppError

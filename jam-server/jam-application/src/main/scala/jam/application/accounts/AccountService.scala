package jam.application.accounts

import cats.Monad
import cats.Monad.ops._

import jam.application.Result._
import jam.application.dsl.AuthDSL
import jam.domains.Id
import jam.domains.auth.Account
import jam.domains.auth.AccountAlreadyExists
import jam.domains.auth.AccountError
import jam.application.AppError
import cats.data.EitherT

class AccountService[F[_]: Monad, Ctx](
    accountRepository: AccountRepository[F, Ctx],
    authDSL: AuthDSL[F]
)(implicit
    ctx: Ctx
) {
  import jam.application.dsl.syntax._

  def create(request: SignUpRequest): F[Either[AccountError, Id[Account]]] = {
    val accountResult: F[Either[AccountError, Account]] =
      for {
        password <- authDSL.createPassword(request.password)
        account = Account.create(
          name = request.name,
          displayName = request.displayName,
          email = request.email,
          password = password
        )
      } yield Right(account)

    def exists(account: Account): F[Either[AccountError, Unit]] =
      accountRepository
        .existsByEmail(account.email)
        .map(exists => !exists)
        .orError(AccountAlreadyExists(account.email))

    def store(account: Account): F[Either[AccountError, Id[Account]]] =
      accountRepository.store(account).map(Right(_))

    val result = for {
      entity    <- accountResult.handleError
      _         <- exists(entity).handleError
      accountId <- store(entity).handleError
    } yield accountId

    result.value
  }
}

case class SignUpRequest(name: String, displayName: Option[String], email: String, password: String)

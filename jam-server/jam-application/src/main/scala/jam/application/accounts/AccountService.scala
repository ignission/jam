package jam.application.accounts

import cats.Monad
import cats.Monad.ops._

import jam.application.Result._
import jam.application.dsl.AuthDSL
import jam.domains.Id
import jam.domains.auth.Account

class AccountService[F[_]: Monad, Ctx](
    accountRepository: AccountRepository[F, Ctx],
    authDSL: AuthDSL[F]
)(implicit
    ctx: Ctx
) {

  def create(request: SignUpRequest): F[Result[Id[Account]]] =
    for {
      password <- authDSL.createPassword(request.password)
      entity = Account.create(
        name = request.name,
        displayName = request.displayName,
        email = request.email,
        password = password
      )
      result <- accountRepository.store(entity).map(Right(_))
    } yield result

}

case class SignUpRequest(name: String, displayName: Option[String], email: String, password: String)

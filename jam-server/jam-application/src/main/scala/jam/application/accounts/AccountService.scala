package jam.application.accounts

import cats.Monad
import cats.Monad.ops._

import jam.application.Result._
import jam.domains.Id
import jam.domains.auth.Account

class AccountService[F[_]: Monad, Ctx](accountRepository: AccountRepository[F, Ctx])(implicit
    ctx: Ctx
) {

  def create(request: SignUpRequest): F[Result[Id[Account]]] = {
    val entity = Account(
      id = Id[Account](0),
      name = request.name
    )

    accountRepository.store(entity).map(Right(_))
  }

}

case class SignUpRequest(name: String, email: String, password: String)

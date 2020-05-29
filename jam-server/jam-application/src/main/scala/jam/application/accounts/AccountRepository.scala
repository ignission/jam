package jam.application.accounts

import jam.domains.Id
import jam.domains.auth.Account

trait AccountRepository[F[_], Ctx] {
  def store(account: Account)(implicit ctx: Ctx): F[Id[Account]]
  def find(id: Id[Account])(implicit ctx: Ctx): F[Option[Account]]
}

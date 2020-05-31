package jam.application.accounts

import jam.domains.Id
import jam.domains.auth.{Account, Email}

trait AccountRepository[F[_], Ctx] {
  def store(account: Account)(implicit ctx: Ctx): F[Id[Account]]
  def find(id: Id[Account])(implicit ctx: Ctx): F[Option[Account]]
  def existsByEmail(email: Email)(implicit ctx: Ctx): F[Boolean]
}

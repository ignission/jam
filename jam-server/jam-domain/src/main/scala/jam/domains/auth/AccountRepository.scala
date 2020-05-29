package jam.domains.auth

import jam.domains.Id

trait AccountRepository[F[_]] {
  def store(account: Account): F[Id[Account]]
  def find(id: Id[Account]): F[Option[Account]]
}

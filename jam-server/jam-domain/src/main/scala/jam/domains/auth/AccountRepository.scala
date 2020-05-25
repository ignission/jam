package jam.domains.auth

import jam.domains.Id

trait AccountRepository[F[_]] {
  def find(id: Id[Account]): F[Option[Account]]
}

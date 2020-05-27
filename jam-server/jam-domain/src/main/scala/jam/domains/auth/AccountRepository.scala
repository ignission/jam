package jam.domains.auth

import simulacrum.typeclass

import jam.domains.Id

import scala.language.implicitConversions

@typeclass
trait AccountRepository[F[_]] {
  def store(account: Account): F[Id[Account]]
  def find(id: Id[Account]): F[Option[Account]]
}

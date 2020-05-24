package jam.dsl

import jam.domains.auth.Password

trait AuthDSL[F[_]] {
  def createPassword(password: String): F[Password]
  def authenticate(password: Password, hashString: String): F[Boolean]
}

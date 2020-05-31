package jam.domains.auth

sealed trait AccountError

case class AccountAlreadyExists(email: Email) extends AccountError

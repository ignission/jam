package jam.domains.auth

import jam.domains.{Entity, Id}

case class Account(
    id: Id[Account],
    name: String,
    displayName: Option[String],
    email: String,
    password: Password
) extends Entity[Account]

object Account {
  def create(
      name: String,
      displayName: Option[String],
      email: String,
      password: Password
  ): Account =
    Account(
      id = Id[Account](0),
      name = name,
      displayName = displayName,
      email = email,
      password = password
    )
}

package jam.domains

case class User(id: Id[User], name: String) extends Entity[User]

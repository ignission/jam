package emoi.server.domains

case class User(id: Id[User], name: String) extends Entity[User]
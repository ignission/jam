package jam.websocket.models

import jam.domains.Id

case class User(
    id: Id[User],
    name: String,
    position: UserPosition
)

object User {
  def create(id: Id[User], name: String): User =
    User(id, name, UserPosition(0, 0))
}

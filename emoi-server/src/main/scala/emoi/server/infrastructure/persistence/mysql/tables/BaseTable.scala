package emoi.server.infrastructure.persistence.mysql.tables

import emoi.server.domains.Types.AnyId
import emoi.server.domains.Entity
import slick.jdbc.MySQLProfile.api._

private[mysql] abstract class BaseTable[A <: Entity](tag: Tag, name: String)
    extends Table[A](tag, name) {

  def id: Rep[AnyId] = column[AnyId]("id", O.PrimaryKey, O.Unique, O.AutoInc)

}

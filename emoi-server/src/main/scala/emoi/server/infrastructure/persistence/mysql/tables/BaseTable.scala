package emoi.server.infrastructure.persistence.mysql.tables

import emoi.server.domains.Id
import slick.jdbc.MySQLProfile.api._
import emoi.server.domains.Entity

private[mysql] abstract class BaseTable[A <: Entity[A]](tag: Tag, name: String)
    extends Table[A](tag, name) {

  import JdbcMapper._

  def id: Rep[Id[A]] = column[Id[A]]("id", O.PrimaryKey, O.Unique, O.AutoInc)

}

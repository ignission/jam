package emoi.server.infrastructure.persistence.mysql.ops

import emoi.server.domains.Entity
import emoi.server.domains.Id
import emoi.server.infrastructure.persistence.mysql.tables.BaseTable
import emoi.server.infrastructure.persistence.mysql.DBIOTypes._
import slick.lifted.TableQuery
import slick.jdbc.SQLiteProfile.api._

trait BaseTableOps[A <: Entity[A], Table <: BaseTable[A]] {

  import emoi.server.infrastructure.persistence.mysql.tables.JdbcMapper._

  protected val tableQuery: TableQuery[Table]

  lazy val createTable = tableQuery.schema.create

  lazy val stream: DBIOStream[A] =
    tableQuery.result

  def select(id: Id[A]): DBIORead[Option[A]] =
    tableQuery.filter(_.id === id).result.headOption

  def write(obj: A): DBIOWrite =
    tableQuery.filter(_.id === obj.id).insertOrUpdate(obj)

  def write(objs: Seq[A]): DBIOWrites = {
    DBIO.sequence(
      objs.map(write)
    )
  }
}

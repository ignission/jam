package emoi.server.infrastructure.persistence.mysql.ops

import emoi.server.domains.Entity
import emoi.server.domains.Id
import emoi.server.infrastructure.persistence.mysql.tables.BaseTable
import emoi.server.infrastructure.persistence.mysql.DBIOTypes._
import slick.lifted.TableQuery
import slick.jdbc.SQLiteProfile.api._

import scala.concurrent.ExecutionContext

trait BaseTableOps[A <: Entity, Table <: BaseTable[A]] {

  protected val tableQuery: TableQuery[Table]

  lazy val createTable = tableQuery.schema.create

  lazy val stream: DBIOStream[A] =
    tableQuery.result

  def select(id: Id[A]): DBIORead[Option[A]] =
    tableQuery.filter(_.id === id.value).result.headOption

  def write(obj: A)(implicit exc: ExecutionContext): DBIOWrite =
    tableQuery.filter(_.id === obj.id).insertOrUpdate(obj)

  def write(objs: Seq[A])(implicit exc: ExecutionContext): DBIOWrites = {
    DBIO.sequence(
      objs.map(write)
    )
  }
}

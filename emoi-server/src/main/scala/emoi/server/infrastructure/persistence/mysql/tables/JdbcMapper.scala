package emoi.server.infrastructure.persistence.mysql.tables

import java.time.{Instant, ZoneId, ZonedDateTime}

import emoi.server.domains.Id
import emoi.server.domains.Types._
import slick.ast.BaseTypedType
import slick.jdbc.JdbcType
import slick.jdbc.SQLiteProfile.api._

object JdbcMapper {

  implicit def idMapper[A] =
    MappedColumnType.base[Id[A], AnyId](
      id => id.value,
      anyid => Id[A](anyid)
    )

  implicit val zonedDateTimeMapper: JdbcType[DateTime] with BaseTypedType[DateTime] =
    MappedColumnType.base[DateTime, Long](
      zonedDateTime => zonedDateTime.toInstant.getEpochSecond,
      epoch => ZonedDateTime.ofInstant(Instant.ofEpochSecond(epoch), ZoneId.systemDefault())
    )
}

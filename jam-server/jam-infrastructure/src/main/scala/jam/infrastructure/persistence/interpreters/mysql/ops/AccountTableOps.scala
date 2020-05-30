package jam.infrastructure.persistence.interpreters.mysql.ops

import cats.data._
import io.getquill.{MysqlMonixJdbcContext, SnakeCase}
import monix.eval.Task

import jam.application.accounts.AccountRepository
import jam.domains.Id
import jam.domains.auth.Account

object AccountTableOps extends AccountRepository[Task, MysqlMonixJdbcContext[SnakeCase]] {
  import jam.infrastructure.persistence.interpreters.mysql.MappingTypes._

  override def store(
      account: Account
  )(implicit ctx: MysqlMonixJdbcContext[SnakeCase]): Task[Id[Account]] = {
    import ctx._
    val q = quote {
      table.insert(lift(account)).onConflictIgnore(_.id)
    }

    ctx.run(q).map(Id[Account](_))
  }

  override def find(id: Id[Account])(implicit
      ctx: MysqlMonixJdbcContext[SnakeCase]
  ): Task[Option[Account]] = {
    import ctx._

    val q = quote {
      table.filter(_.id == lift(id))
    }

    ctx.run(q).map(_.headOption)
  }

  private def table(implicit ctx: MysqlMonixJdbcContext[SnakeCase]) =
    ctx.quote {
      ctx.querySchema[Account](
        "accounts",
        _.id          -> "id",
        _.name        -> "name",
        _.displayName -> "display_name",
        _.email       -> "email",
        _.password    -> "password"
      )
    }
}

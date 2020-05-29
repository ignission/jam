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
      query[Account].insert(lift(account)).onConflictIgnore(_.id)
    }

    ctx.run(q).map(Id[Account](_))
  }

  override def find(id: Id[Account])(implicit
      ctx: MysqlMonixJdbcContext[SnakeCase]
  ): Task[Option[Account]] = {
    import ctx._

    val q = quote {
      query[Account].filter(_.id == lift(id))
    }

    ctx.run(q).map(_.headOption)
  }
}

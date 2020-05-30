package jam.infrastructure.persistence.interpreters.mysql.tables

import io.getquill.{MysqlMonixJdbcContext, SnakeCase}

import jam.domains.auth.Account

object AccountTable {
  def table(implicit ctx: MysqlMonixJdbcContext[SnakeCase]) =
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

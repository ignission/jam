package jam.infrastructure.persistence.interpreters.mysql

import cats.data._
import io.getquill.{MysqlMonixJdbcContext, SnakeCase}
import monix.eval.Task

object types {
  type Query[A] = Kleisli[Task, MysqlMonixJdbcContext[SnakeCase], A]
}

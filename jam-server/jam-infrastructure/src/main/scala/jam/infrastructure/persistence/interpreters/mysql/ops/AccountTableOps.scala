package jam.infrastructure.persistence.interpreters.mysql.ops

import cats.data._

import jam.domains.Id
import jam.domains.auth.{Account, AccountRepository}
import jam.infrastructure.persistence.interpreters.mysql.types._

object AccountTableOps extends AccountRepository[Query] {
  import jam.infrastructure.persistence.interpreters.mysql.MappingTypes._

  override def find(id: Id[Account]): Query[Option[Account]] =
    Kleisli { implicit ctx =>
      import ctx._

      val q = ctx.quote {
        query[Account].filter(_.id == lift(id))
      }

      ctx.run(q).map(_.headOption)
    }

}

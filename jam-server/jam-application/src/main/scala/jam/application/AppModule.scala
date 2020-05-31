package jam.application

import jam.application.accounts.AccountModule
import jam.application.sessions.SessionModule

trait AppModule[F[_], Ctx] {
  val accountModule: AccountModule[F, Ctx]
  val sessionModule: SessionModule[F]
}

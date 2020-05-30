package jam.application

import jam.application.accounts.AccountModule

trait AppModule[F[_], Ctx] {
  val accountModule: AccountModule[F, Ctx]
}

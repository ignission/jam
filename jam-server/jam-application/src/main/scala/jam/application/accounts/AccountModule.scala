package jam.application.accounts

trait AccountModule[F[_], Ctx] {
  val accountService: AccountService[F, Ctx]
}

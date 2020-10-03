package jam.application.dsl

object Result {
  type Result[F[_], E, A] = F[Either[E, A]]
}

package jam.application.sessions

trait SessionModule[F[_]] {
  val sessionService: SessionService[F]
}

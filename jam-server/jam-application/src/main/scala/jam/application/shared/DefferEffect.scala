package jam.application.shared

import monix.eval.Task

trait DeferEffect[F[_]] {
  def deferEffect[A](a: => A): F[A]
}

object DeferEffectInstance {
  implicit val taskDefer = new DeferEffect[Task] {
    override def deferEffect[A](a: => A): Task[A] =
      Task(a)
  }
}

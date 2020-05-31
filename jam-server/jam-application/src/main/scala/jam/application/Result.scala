package jam.application

object Result {
  type Result[A] = Either[AppError, A]
}

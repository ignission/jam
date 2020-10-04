package jam.websocket.dsl

import cats.Monad

import jam.application.dsl.Result.Result
import jam.application.dsl.{LogDSL, Result, StoreDSL}
import jam.domains.{Id, User}
import jam.websocket.dsl.UserDSL.UserStore
import jam.websocket.{AppError, LoggingFailed, NickNameAlreadyTaken}

object UserDSL {
  type UserStore[F[_]] = StoreDSL[F, Id[User], AppError, User]
}

class UserDSL[F[_]: Monad](userStore: UserStore[F], logDSL: LogDSL[F]) {
  import jam.application.dsl.syntax._

  def addUser(user: User): Result[F, AppError, User] = {
    val result = for {
      _      <- logDSL.trace(s"Adding user ${user.name}").mapError(LoggingFailed(_)).handleError
      exists <- checkNameExists(user.name).handleError
      _ <-
        if (exists) Result.error(NickNameAlreadyTaken(user.name)).handleError
        else Result.success(exists).handleError
      newUser <- userStore.put(user.id, user).handleError
    } yield newUser

    result.value
  }

  private def checkNameExists(name: String): Result[F, AppError, Boolean] = {
    val result = for {
      allUsers <- userStore.getAll.handleError
      exists = allUsers.exists(_.name == name)
    } yield exists

    result.value
  }

}

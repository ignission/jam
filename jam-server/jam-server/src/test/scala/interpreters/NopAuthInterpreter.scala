package interpreters

import monix.eval.Task

import jam.application.dsl.AuthDSL
import jam.domains.auth.Password

class NopAuthInterpreter extends AuthDSL[Task] {
  override def createPassword(password: String): Task[Password] =
    Task(Password(password))

  override def authenticate(password: Password, hashString: String): Task[Boolean] =
    Task(password.value == hashString)
}

package jam.interpreters

import monix.eval.Task
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

import jam.domains.auth.Password
import jam.dsl.AuthDSL

class AuthInterpreter() extends AuthDSL[Task] {

  private val encoder = new BCryptPasswordEncoder()

  override def createPassword(password: String): Task[Password] =
    Task {
      Password(encoder.encode(password))
    }

  override def authenticate(password: Password, hashString: String): Task[Boolean] =
    Task {
      encoder.matches(password.value, hashString)
    }

}

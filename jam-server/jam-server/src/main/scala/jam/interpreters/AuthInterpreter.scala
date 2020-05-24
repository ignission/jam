package jam.interpreters

import jam.dsl.AuthDSL
import monix.eval.Task
import jam.domains.auth.Password
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

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

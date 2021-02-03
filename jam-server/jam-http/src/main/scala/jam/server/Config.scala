package jam.server

import com.typesafe.config._

sealed trait Mode

object Mode {
  case object Production extends Mode
  case object Local      extends Mode
  case object Test       extends Mode

  def from(str: String): Mode =
    str match {
      case "production" => Production
      case "local"      => Local
      case "test"       => Test
      case _ =>
        throw new RuntimeException(
          s"Invalid mode. Input: [$str] Available [ production | local | test ]"
        )
    }
}

object Config {
  private val config = ConfigFactory.load()

  val interface = config.getString("app.interface")
  val port      = config.getInt("app.port")
  val mode      = Mode.from(config.getString("app.mode"))

  object OpenVidu {
    object Server {
      val url      = config.getString("openvidu.server.url")
      val username = config.getString("openvidu.server.username")
      val password = config.getString("openvidu.server.password")
    }
  }
}

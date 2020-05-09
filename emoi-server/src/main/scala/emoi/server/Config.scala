package emoi.server

import com.typesafe.config._

object Config {
  private val config = ConfigFactory.load()

  object OpenVidu {
    object Server {
      val url      = config.getString("openvidu.server.url")
      val username = config.getString("openvidu.server.username")
      val password = config.getString("openvidu.server.password")
    }
  }
}

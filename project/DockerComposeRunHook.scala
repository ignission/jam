import play.sbt.PlayRunHook

import scala.sys.process._
import java.io.File

object DockerComposeRunHook {
  def apply(base: File): PlayRunHook = {
    object DockerCompose extends PlayRunHook {
      override def beforeStarted(): Unit = {
        Process("docker compose up -d redis", base).!
      }
      override def afterStopped(): Unit = {
        Process("docker-compose kill", base).!
      }
    }
    DockerCompose
  }
}

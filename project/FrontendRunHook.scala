import play.sbt.PlayRunHook

import java.io.File
import scala.sys.process._

object FrontendRunHook {
  def apply(base: File): PlayRunHook = {
    object Yarn extends PlayRunHook {
      var process: Option[Process] = None

      override def beforeStarted(): Unit = {
        Process("yarn", base).!
      }

      override def afterStarted(): Unit = {
        process = Some(Process("yarn hot", base).run)
      }

      override def afterStopped(): Unit = {
        process.foreach(p => p.destroy())
        process = None
      }
    }
    Yarn
  }
}

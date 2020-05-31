package jam.shared

import com.typesafe.config.ConfigFactory
import org.flywaydb.core.Flyway
import org.scalactic.source.Position
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll}

case class DatabaseConfig() {
  private val config = ConfigFactory.load()

  val url      = config.getString("ctx.dataSource.url")
  val user     = config.getString("ctx.dataSource.user")
  val password = config.getString("ctx.dataSource.password")
}

trait WithDatabase extends AnyWordSpec with BeforeAndAfterAll {
  val config = DatabaseConfig()
  val flyway = Flyway.configure().dataSource(config.url, config.user, config.password).load()

  override def beforeAll(): Unit =
    flyway.migrate()

  override def afterAll(): Unit =
    flyway.clean()

}

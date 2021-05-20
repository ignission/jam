package modules

import com.google.inject.AbstractModule
import com.redis.RedisClientPool
import play.api.{Configuration, Environment, Mode}

class DefaultModule(environment: Environment, configuration: Configuration)
    extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[RedisClientPool]).toInstance(
      new RedisClientPool(
        configuration.get[String]("redis.host"),
        configuration.get[Int]("redis.port")
      )
    )
    if (environment.mode == Mode.Prod) () else ()
  }
}

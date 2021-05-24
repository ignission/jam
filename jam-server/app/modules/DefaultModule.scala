package modules

import com.google.inject.AbstractModule
import com.redis.RedisClientPool
import infrastructure.RoomRepositoryOnMemory
import play.api.{Configuration, Environment, Mode}
import services.RoomRepository

class DefaultModule(environment: Environment, configuration: Configuration)
    extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[RedisClientPool]).toInstance(
      new RedisClientPool(
        configuration.get[String]("redis.host"),
        configuration.get[Int]("redis.port")
      )
    )

    bind(classOf[RoomRepository]).to(classOf[RoomRepositoryOnMemory])

    if (environment.mode == Mode.Prod) () else ()
  }
}

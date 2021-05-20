package infrastructure

import javax.inject.{Inject, Singleton}

import com.redis.RedisClientPool

object RedisClient {
  def create(host: String, port: Int): RedisClient =
    new RedisClient(
      new RedisClientPool(host, port)
    )
}

@Singleton
class RedisClient @Inject() (redis: RedisClientPool) {

  def get(key: String): Option[String] =
    redis.withClient { client =>
      client.get(key)
    }

  def getAll: Seq[String] =
    redis.withClient { client =>
      client.keys("*").getOrElse(Seq()).flatten.flatMap { key =>
        get(key)
      }
    }

  def put(key: String, value: String): Unit =
    redis.withClient { client =>
      client.set(key, value)
    }

  def delete(key: String): Boolean =
    redis.withClient { client =>
      client.del(key).exists(_ > 0)
    }
}

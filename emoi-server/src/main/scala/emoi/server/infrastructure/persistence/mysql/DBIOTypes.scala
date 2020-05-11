package emoi.server.infrastructure.persistence.mysql

import slick.dbio.Effect.{Read, Schema, Write}
import slick.dbio.{DBIOAction, NoStream, StreamingDBIO}

// https://scala-slick.org/doc/3.3.2/dbio.html
object DBIOTypes {
  type DBIORead[X]   = DBIOAction[X, NoStream, Read]
  type DBIOWrite     = DBIOAction[Int, NoStream, Write]
  type DBIOWrites    = DBIOAction[Seq[Int], NoStream, Write]
  type DBIOStream[A] = StreamingDBIO[Seq[A], A]
  type DBIOSchema    = DBIOAction[Unit, NoStream, Schema]
}

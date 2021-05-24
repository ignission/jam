package infrastructure

import akka.NotUsed
import akka.stream.scaladsl.{Sink, Source}
import domain.models.UserCommand

case class RoomChannel(
    sink: Sink[UserCommand, NotUsed],
    source: Source[UserCommand, NotUsed]
)

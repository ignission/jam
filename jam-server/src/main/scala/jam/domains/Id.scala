package emoi.server.domains

import emoi.server.domains.Types.AnyId

case class Id[T] private (value: AnyId) extends AnyVal

object Id {}

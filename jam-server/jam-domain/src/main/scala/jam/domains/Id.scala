package jam.domains

import jam.domains.Types.AnyId

case class Id[T] private (value: AnyId) extends AnyVal

object Id {}

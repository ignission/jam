package jam.infrastructure.persistence.interpreters.mysql

import io.getquill.MappedEncoding

import jam.domains.Id
import jam.domains.Types.AnyId

object MappingTypes {
  implicit def encodeId[A] = MappedEncoding[Id[A], AnyId](_.value)
  implicit def decodeid[A] = MappedEncoding[AnyId, Id[A]](Id[A](_))
}

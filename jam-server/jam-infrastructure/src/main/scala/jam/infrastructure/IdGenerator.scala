package jam.infrastructure

import monix.execution.atomic.AtomicLong

import jam.domains.Id

object IdGenerator {

  type IdGenerator[A] = () => Id[A]

  def createGenerator[A](): IdGenerator[A] = {
    val id = AtomicLong(0)
    () => {
      id.add(1)
      Id[A](id.get())
    }
  }
}

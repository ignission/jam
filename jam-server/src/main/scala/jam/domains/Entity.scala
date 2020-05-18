package jam.domains

trait Entity[A] {
  def id: Id[A]
}

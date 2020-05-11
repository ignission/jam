package emoi.server.domains

trait Entity[A] {
  def id: Id[A]
}

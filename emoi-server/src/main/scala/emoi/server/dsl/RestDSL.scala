package emoi.server.dsl

import emoi.server.dsl.RestDSL.Result
import tech.ignission.openvidu4s.core.datas.Session

trait RestDSL[F[_]] {
  def listSessions: Result[F, Seq[Session]]
}

object RestDSL {
  type Result[F[_], A] = F[Either[AppError, A]]
}

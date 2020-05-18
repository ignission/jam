package tech.ignission.openvidu4s.core.datas

sealed trait Role

object Role {

  /**
    * SUBSCRIBER + PUBLISHER permissions + can force the unpublishing or
    * isconnection over a third-party Stream or Connection (call Session.forceUnpublish()
    * and Session.forceDisconnect())
    */
  case object MODERATOR extends Role

  /**
    * SUBSCRIBER permissions + can publish their own Streams (call Session.publish())
    */
  case object PUBLISHER extends Role

  /**
    * Can subscribe to published Streams of other users
    */
  case object SUBSCRIBER extends Role
}

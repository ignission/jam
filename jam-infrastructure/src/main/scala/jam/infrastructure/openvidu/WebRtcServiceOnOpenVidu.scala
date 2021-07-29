package jam.infrastructure.openvidu

//import jam.application.services.WebRtcService
//import jam.domain.models.{Session, Token}
//import sttp.client3._
//import sttp.client3.circe._
//import io.circe.generic.auto._
//import sttp.client3.asynchttpclient.zio._

import java.time.ZonedDateTime

object WebRtcServiceOnOpenVidu {
//  import CirceJsonFormats._

//        private val resource = s"$baseUrl/api/sessions"

//  def live(baseUrl: String, username: String, password: String): WebRtcService =
//    new WebRtcService {
//      private val request  = basicRequest.auth.basic(username, password)
//
//      override def createSession(id: String): Session = {
//        val req = request
//          .post(uri"$resource")
//          .body(InitializeSession(id))
//          .response(asJson[InitializedSession])
//
//        send(req)
//      }
//
//      override def getToken(session: Session): Token =
//        ???
//    }
}

private[openvidu] case class InitializeSession(customSessionId: String)
private[openvidu] case class InitializedSession(id: String, createdAt: ZonedDateTime)

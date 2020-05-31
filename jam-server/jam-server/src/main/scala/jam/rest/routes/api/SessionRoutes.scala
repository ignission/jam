package jam.rest.routes.api

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import monix.eval.Task
import monix.execution.Scheduler
import spray.json._

import jam.application.Result.Result
import jam.application.sessions.SessionModule
import jam.rest.routes.CreateSessionRequest

import tech.ignission.openvidu4s.core.datas.{GeneratedToken, InitializedSession, Session, SessionId}

class SessionRoutes(module: SessionModule[Task])(implicit s: Scheduler)
    extends SprayJsonSupport
    with DefaultJsonProtocol {
  import jam.rest.formatters.SprayJsonFormats._
  import jam.rest.routes.ResponseHandler._

  private val service = module.sessionService

  def routes: Route =
    sessionRoutes ~ tokenRoutes

  private def sessionRoutes: Route =
    path("sessions") {
      concat(
        get {
          val taskResult: Task[Result[Seq[Session]]] = service.listSessions

          taskResult.handleResponse.toRoute
        },
        post {
          entity(as[CreateSessionRequest]) { req =>
            val taskResult: Task[Result[InitializedSession]] = service.createSession(req.sessionId)

            taskResult.handleResponse.toRoute
          }
        }
      )
    }

  private def tokenRoutes: Route =
    pathPrefix("tokens" / ".+".r) { sessionId =>
      post {
        val taskResult: Task[Result[GeneratedToken]] = service.generateToken(SessionId(sessionId))

        taskResult.handleResponse.toRoute
      }
    }

}

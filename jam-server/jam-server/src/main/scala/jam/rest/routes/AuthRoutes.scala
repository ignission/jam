package jam.rest.routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import cats.Monad
import monix.eval.Task
import monix.execution.Scheduler
import spray.json._

import jam.application.accounts.{AccountModule, SignUpRequest}

class AuthRoutes[Ctx](accountModule: AccountModule[Task, Ctx])(implicit s: Scheduler)
    extends SprayJsonSupport
    with DefaultJsonProtocol {
  import jam.rest.formatters.SprayJsonFormats._
  import jam.rest.routes.ResponseHandler._

  private val accountService = accountModule.accountService

  def routes: Route =
    pathPrefix("auth") {
      signUpRoute
    }

  private def signUpRoute: Route =
    path("signup") {
      post {
        entity(as[SignUpRequest]) { request =>
          accountService.create(request).handleResponse.toRoute
        }
      }
    }
}

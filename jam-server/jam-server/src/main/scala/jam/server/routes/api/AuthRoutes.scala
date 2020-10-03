package jam.rest.routes.api

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import monix.eval.Task
import monix.execution.Scheduler
import spray.json._

import jam.application.accounts.{AccountModule, SignUpRequest}
import jam.application.dsl.Result.Result
import jam.application.{AccountServiceError, AppError}
import jam.domains.Id
import jam.domains.auth.Account

class AuthRoutes[Ctx](accountModule: AccountModule[Task, Ctx])(implicit s: Scheduler)
    extends SprayJsonSupport
    with DefaultJsonProtocol {
  import jam.server.formatters.SprayJsonFormats._
  import jam.rest.routes.ResponseHandler._
  import jam.application.dsl.syntax._

  private val accountService = accountModule.accountService

  def routes: Route =
    pathPrefix("auth") {
      signUpRoute
    }

  private def signUpRoute: Route =
    path("signup") {
      post {
        entity(as[SignUpRequest]) { request =>
          val taskResult: Result[Task, AppError, Id[Account]] =
            accountService.create(request).mapError(e => AccountServiceError(e))

          taskResult.map(result => result.map(_ => ())).handleResponse.toRoute
        }
      }
    }
}

package jam.rest.routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import cats.Monad
import monix.eval.Task
import monix.execution.Scheduler
import spray.json._

import jam.application.accounts.{AccountModule, SignUpRequest}
import jam.application.AccountServiceError
import jam.application.Result.Result
import jam.domains.Id
import jam.domains.auth.Account

class AuthRoutes[Ctx](accountModule: AccountModule[Task, Ctx])(implicit s: Scheduler)
    extends SprayJsonSupport
    with DefaultJsonProtocol {
  import jam.rest.formatters.SprayJsonFormats._
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
          val taskResult: Task[Result[Id[Account]]] =
            accountService.create(request).mapError(e => AccountServiceError(e))

          taskResult.map(result => result.map(_ => ())).handleResponse.toRoute
        }
      }
    }
}

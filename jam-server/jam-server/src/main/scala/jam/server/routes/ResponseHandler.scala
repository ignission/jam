package jam.rest.routes

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.StandardRoute
import monix.eval.Task
import monix.execution.Scheduler
import spray.json._

import jam.application.dsl.Result.Result
import jam.application.{AccountServiceError, AppError, OpenViduClientError}
import jam.domains.auth.AccountAlreadyExists

import tech.ignission.openvidu4s.core.dsl.{AlreadyExists, RequestError, ServerDown}

object ResponseHandler {
  implicit class ResponseHandler[A](result: Result[Task, AppError, A]) {
    def handleResponse(implicit formatter: JsonWriter[A]): Task[HttpResponse] = {
      result.map {
        case Right(value) =>
          value match {
            case _: Unit =>
              HttpResponse(
                StatusCodes.OK,
                entity = "{}" // hack: scalafix doesn't recognize JsonWriter with Unit
              )
            case v =>
              HttpResponse(StatusCodes.OK, entity = v.toJson.compactPrint)
          }
        case Left(error: OpenViduClientError) =>
          error.inner match {
            case AlreadyExists =>
              HttpResponse(StatusCodes.Conflict)
            case RequestError(msg) =>
              HttpResponse(StatusCodes.BadRequest, entity = msg)
            case ServerDown =>
              HttpResponse(StatusCodes.BadGateway)
          }
        case Left(_: jam.application.InternalError) =>
          HttpResponse(StatusCodes.InternalServerError)
        case Left(error: AccountServiceError) =>
          error.inner match {
            case AccountAlreadyExists(_) =>
              HttpResponse(StatusCodes.BadRequest)
          }
      }
    }
  }

  implicit class HttpResponseOps[A](response: Task[HttpResponse]) {
    def toRoute(implicit s: Scheduler): StandardRoute =
      complete(response.runToFuture)
  }
}

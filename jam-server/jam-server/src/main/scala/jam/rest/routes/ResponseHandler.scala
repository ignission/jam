package jam.rest.routes

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.StandardRoute
import cats.Monad
import cats.Monad.ops._
import monix.eval.Task
import monix.execution.Scheduler
import spray.json._

import jam.application.Result.Result
import jam.application.{AppError, OpenViduClientError}

import tech.ignission.openvidu4s.core.dsl.{AlreadyExists, RequestError, ServerDown}

import scala.concurrent.Future

object ResponseHandler {
  implicit class ResponseHandler[F[_]: Monad, A](result: F[Result[A]]) {
    def handleResponse(implicit formatter: JsonWriter[A]): F[HttpResponse] = {
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
      }
    }
  }

  implicit class HttpResponseOps[A](response: Task[HttpResponse]) {
    def toRoute(implicit s: Scheduler): StandardRoute =
      complete(response.runToFuture)
  }
}

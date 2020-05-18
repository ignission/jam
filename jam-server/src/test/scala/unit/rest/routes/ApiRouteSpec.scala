package unit.rest.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import jam.dsl.RestDSL
import jam.rest.routes.ApiRoute
import interpreters.NopRestInterpreter
import monix.eval.Task
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ApiRouteSpec extends AnyWordSpec with Matchers with ScalatestRouteTest {

  val restDSL: RestDSL[Task] = new NopRestInterpreter()
  val routes: Route          = new ApiRoute(restDSL)(monix.execution.Scheduler.Implicits.global).routes

  "ApiRoute" should {
    "return a session list for GET requests" in {
      Get("/rest/api/v1/sessions") ~> routes ~> check {
        status shouldEqual StatusCodes.OK
        entityAs[String] shouldEqual
          """{"sessions":[{"createdAt":1589035535985,"sessionId":"test-session1"}]}"""
      }
    }

    "return a valid result for POST requests to the tokens" in {
      Post("/rest/api/v1/tokens/session-a") ~> routes ~> check {
        status shouldEqual StatusCodes.OK
        entityAs[String] shouldEqual
          """{"token":"abcdefg"}"""
      }
    }

    "return a NotFound error for GET requests to the tokens" in {
      Get("/rest/api/v1/tokens") ~> routes ~> check {
        status shouldEqual StatusCodes.NotFound
      }
      Post("/rest/api/v1/tokens") ~> routes ~> check {
        status shouldEqual StatusCodes.NotFound
      }
    }

    "return a NotFound error for a request to undefined routes" in {
      Get("/rest/ap") ~> routes ~> check {
        status shouldEqual StatusCodes.NotFound
      }
      Get("/rest/api/v111/sessions") ~> routes ~> check {
        status shouldEqual StatusCodes.NotFound
      }
      Get("/rest/api/v1/token") ~> routes ~> check {
        status shouldEqual StatusCodes.NotFound
      }
    }
  }
}

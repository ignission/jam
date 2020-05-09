package unit.rest.routes

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import emoi.server.rest.routes.ApiRoute
import monix.execution.Scheduler.Implicits.global

class ApiRouteSpec extends AnyWordSpec with Matchers with ScalatestRouteTest {

  "ApiRoute" should {
    "return a session list for GET requests" in {
      Get("/rest/api/v1/sessions") ~> ApiRoute.routes ~> check {
        status shouldEqual StatusCodes.OK
      }
    }

    "return a valid result for POST requests to the tokens" in {
      Post("/rest/api/v1/tokens/session-a") ~> ApiRoute.routes ~> check {
        status shouldEqual StatusCodes.OK
      }
    }

    "return a NotFound error for GET requests to the tokens" in {
      Get("/rest/api/v1/tokens") ~> ApiRoute.routes ~> check {
        status shouldEqual StatusCodes.NotFound
      }
      Post("/rest/api/v1/tokens") ~> ApiRoute.routes ~> check {
        status shouldEqual StatusCodes.NotFound
      }
    }

    "return a NotFound error for a request to undefined routes" in {
      Get("/rest/ap") ~> ApiRoute.routes ~> check {
        status shouldEqual StatusCodes.NotFound
      }
      Get("/rest/api/v111/sessions") ~> ApiRoute.routes ~> check {
        status shouldEqual StatusCodes.NotFound
      }
      Get("/rest/api/v1/token") ~> ApiRoute.routes ~> check {
        status shouldEqual StatusCodes.NotFound
      }
    }
  }
}

package unit.rest.routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{StatusCodes, _}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import interpreters.{NopAuthInterpreter, NopRestInterpreter}
import io.getquill._
import io.getquill.context.monix.Runner
import monix.eval.Task
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import jam.application.AppModule
import jam.application.accounts.{AccountModule, AccountService}
import jam.dsl.RestDSL
import jam.infrastructure.persistence.interpreters.mysql.ops.AccountTableOps
import jam.rest.routes.{ApiRoute, CreateSessionRequest}

import tech.ignission.openvidu4s.core.datas.SessionId
import jam.application.accounts.SignUpRequest

class ApiRouteSpec extends AnyWordSpec with Matchers with ScalatestRouteTest {

  import jam.rest.formatters.SprayJsonFormats._

  implicit val exc = monix.execution.Scheduler.Implicits.global
  implicit val ctx: MysqlMonixJdbcContext[SnakeCase] =
    new MysqlMonixJdbcContext(SnakeCase, "ctx", Runner.using(exc))

  private val accountRepository = AccountTableOps
  private val authDSL           = new NopAuthInterpreter
  private val appModule = new AppModule[Task, MysqlMonixJdbcContext[SnakeCase]] {
    override val accountModule: AccountModule[Task, MysqlMonixJdbcContext[SnakeCase]] =
      new AccountModule[Task, MysqlMonixJdbcContext[SnakeCase]] {
        override val accountService: AccountService[Task, MysqlMonixJdbcContext[SnakeCase]] =
          new AccountService(accountRepository, authDSL)
      }
  }

  val restDSL: RestDSL[Task] = new NopRestInterpreter()
  val routes: Route = new ApiRoute(restDSL, appModule)(
    monix.execution.Scheduler.Implicits.global
  ).routes

  "ApiRoute" should {
    "return a session list for GET requests" in {
      Get("/rest/api/v1/sessions") ~> routes ~> check {
        status shouldEqual StatusCodes.OK
        entityAs[String] shouldEqual
          """{"sessions":[{"createdAt":1589035535985,"sessionId":"test-session1"}]}"""
      }
    }

    "return a valid sesion for POST requests" in {
      val entity = Marshal(CreateSessionRequest(SessionId("test-session"))).to[MessageEntity]

      Post("/rest/api/v1/sessions", entity) ~> routes ~> check {
        status shouldEqual StatusCodes.OK
        entityAs[String] shouldEqual
          """{"session":{"createdAt":1589035535985,"id":"test-session"}}"""
      }
    }

    "return an account id for POST sign up" in {
      val entity =
        Marshal(SignUpRequest("abc", Some("shoma"), "email", "password")).to[MessageEntity]

      Post("/rest/api/v1/auth/signup", entity) ~> routes ~> check {
        status shouldEqual StatusCodes.OK
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

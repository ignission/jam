package unit.rest.routes

import java.time.{Instant, ZoneId, ZonedDateTime}

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{StatusCodes, _}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import interpreters.NopAuthInterpreter
import io.getquill._
import io.getquill.context.monix.Runner
import monix.eval.Task
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import jam.application.accounts.{AccountModule, AccountService, SignUpRequest}
import jam.application.sessions.{SessionModule, SessionService}
import jam.application.{AppModule, OpenViduClientError}
import jam.infrastructure.persistence.interpreters.mysql.ops.AccountTableOps
import jam.rest.routes.{ApiRoute, CreateSessionRequest}
import jam.shared.WithDatabase

import tech.ignission.openvidu4s.akka.interpreters.OpenViduHttpDSLOnAkka
import tech.ignission.openvidu4s.core.Basic
import tech.ignission.openvidu4s.core.apis.AllAPI
import tech.ignission.openvidu4s.core.datas.{
  GeneratedToken,
  InitializedSession,
  Session,
  SessionId,
  Token
}

class ApiRouteSpec extends AnyWordSpec with Matchers with ScalatestRouteTest with WithDatabase {

  import jam.server.formatters.SprayJsonFormats._

  implicit val exc = monix.execution.Scheduler.Implicits.global
  implicit val ctx: MysqlMonixJdbcContext[SnakeCase] =
    new MysqlMonixJdbcContext(SnakeCase, "ctx", Runner.using(exc))

  private val accountRepository = AccountTableOps
  private val authDSL           = new NopAuthInterpreter
  private val akkaHttpDSL       = new OpenViduHttpDSLOnAkka()
  private val credential        = Basic("", "")
  private val openviduAPI       = new AllAPI("", credential)(akkaHttpDSL)
  private val appModule = new AppModule[Task, MysqlMonixJdbcContext[SnakeCase]] {
    override val accountModule: AccountModule[Task, MysqlMonixJdbcContext[SnakeCase]] =
      new AccountModule[Task, MysqlMonixJdbcContext[SnakeCase]] {
        override val accountService: AccountService[Task, MysqlMonixJdbcContext[SnakeCase]] =
          new AccountService(accountRepository, authDSL)
      }
    override val sessionModule: SessionModule[Task] = new SessionModule[Task] {
      override val sessionService: SessionService[Task] = new SessionService[Task](openviduAPI) {
        override def listSessions: Task[Either[OpenViduClientError, Seq[Session]]] =
          Task {
            Right(
              Seq(
                Session(
                  id = SessionId("test-session1"),
                  ZonedDateTime
                    .ofInstant(Instant.ofEpochMilli(1589035535985L), ZoneId.systemDefault())
                )
              )
            )
          }

        override def generateToken(
            sessionId: SessionId
        ): Task[Either[OpenViduClientError, GeneratedToken]] =
          Task {
            Right(
              GeneratedToken(
                sessionId = sessionId,
                token = Token("abcdefg")
              )
            )
          }

        override def createSession(
            sessionId: SessionId
        ): Task[Either[OpenViduClientError, InitializedSession]] =
          Task {
            Right(
              InitializedSession(
                id = sessionId,
                ZonedDateTime
                  .ofInstant(Instant.ofEpochMilli(1589035535985L), ZoneId.systemDefault())
              )
            )
          }
      }
    }
  }

  val routes: Route = new ApiRoute(appModule)(
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

  "Auth route" should {
    val path = "/rest/api/v1/auth/signup"

    "success when POST signup" in {
      val entity =
        Marshal(SignUpRequest("abc", Some("shoma"), "email@email.com", "password"))
          .to[MessageEntity]

      Post(path, entity) ~> routes ~> check {
        status shouldEqual StatusCodes.OK
      }
    }

    "returns bad request when POST signup with same email" in {
      val entity =
        Marshal(SignUpRequest("abc", Some("shoma"), "email", "password")).to[MessageEntity]

      Post(path, entity) ~> routes ~> check {
        status shouldEqual StatusCodes.OK
      }

      Post(path, entity) ~> routes ~> check {
        status shouldEqual StatusCodes.BadRequest
      }
    }
  }
}

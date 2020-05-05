package emoi.server.rest

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

object StaticRoute {
  def routes: Route = htmlRoute ~ jsRoutes ~ imageRoutes

  private def htmlRoute: Route =
    get {
      pathEndOrSingleSlash {
        getFromResource("static/index.html")
      }
    }

  private def jsRoutes: Route =
    pathPrefix(".+.js".r) { str =>
      get {
        getFromResource(s"static/$str")
      }
    }

  private def imageRoutes: Route =
    pathPrefix("images" / ".+".r) { str =>
      get {
        getFromResource(s"static/images/$str")
      }
    }
}

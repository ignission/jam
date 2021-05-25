package controllers

import javax.inject._

import play.api.http.HttpErrorHandler
import play.api.mvc._

@Singleton
class AssetController @Inject() (
    assets: Assets,
    errorHandler: HttpErrorHandler,
    val controllerComponents: ControllerComponents
) extends BaseController {

  def index: Action[AnyContent] =
    assets.at("index.html")

  def assetOrDefault(resource: String): Action[AnyContent] =
    if (resource.startsWith("api") || resource.startsWith("ws"))
      Action.async(r => errorHandler.onClientError(r, NOT_FOUND, "Not found"))
    else if (resource.contains("."))
      assets.at(resource)
    else
      index
}

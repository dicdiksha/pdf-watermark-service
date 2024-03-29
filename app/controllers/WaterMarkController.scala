package controllers

import model._
import model.request.WaterMarkRequest
import service.WaterMarkService
import util.Constants
import implicits._

import javax.inject._
import play.api.libs.json._
import play.api.libs.ws.WSClient
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's.
 */
@Singleton
class WaterMarkController @Inject()(val controllerComponents: ControllerComponents, ws: WSClient)
  extends BaseController with JsonProtocols {

  /**
   * Create an Action to render an path for watermark pdf.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `POST` request with
   * a path of `/content/pdf/v1/create/:identifier`.
   */
  def createWaterMark(identifier: String): Action[JsValue] = Action.async(parse.json) { implicit request =>
     request.body.validate[WaterMarkRequest]
       .fold(
         errors =>
           Future.successful(
             BadRequest(
               Json.obj("message" -> "Invalid JSON", "errors" -> JsError.toJson(errors))
             )
           ),
         waterMarkRequest => {
           println(s"Request Payload: ${Json.toJson(waterMarkRequest)}")
           WaterMarkService()
             .addWatermark(identifier,waterMarkRequest.filePath, Constants.OUTPUTPATH, Constants.WATERMARKTEXT)(ws)
             .toAsync()
         }
       )
  }

  /**
   * Create an Action to render an server health status
   * path:- /health
   * @return success
   */
  def healthCheck: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(Json.toJson(Map("message"->"success")))
  }

}

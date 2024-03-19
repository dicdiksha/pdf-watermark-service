package implicits

import model.JsonProtocols
import model.response.{ErrorResponse, ResponseJsonProtocols}
import play.api.libs.json.{JsValue, Json, Writes}
import play.api.mvc.Result
import play.api.mvc.Results.{InternalServerError, NotFound, Ok}

import java.io.FileNotFoundException
import scala.concurrent.{ExecutionContext, Future}

private[implicits] trait FutureImplicits {

  type Async[T] = Future[Result]
  implicit class FutureOps[T](future: Future[T]) extends ResponseJsonProtocols {
    def toAsync()(implicit ex: ExecutionContext, tjs: Writes[T]): Async[T] =
      future.map{ res=>
       val response = Json.toJson(res)
       println(s"Response Result: $response")
        Ok(response)
      }.recover {
        case fileNotFoundExp: FileNotFoundException =>
          NotFound(Json.toJson(ErrorResponse(404,fileNotFoundExp.getMessage)))
        case ex: Exception =>
          ex.printStackTrace()
          InternalServerError(Json.toJson(ErrorResponse(500,ex.getMessage)))
      }
  }
}

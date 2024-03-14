package implicits

import play.api.libs.json.{JsValue, Json, Writes}
import play.api.mvc.Result
import play.api.mvc.Results.Ok

import scala.concurrent.{ExecutionContext, Future}

private[implicits] trait FutureImplicits {

  type Async[T] = Future[Result]
  implicit class FutureOps[T](future: Future[T]) {
    def toAsync()(implicit ex: ExecutionContext, tjs: Writes[T]): Async[T] =
      future.map(x=> Ok(Json.toJson(x)))
  }
}

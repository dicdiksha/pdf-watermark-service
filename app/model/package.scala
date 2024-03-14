import model.request.RequestJsonProtocols
import model.response.ResponseJsonProtocols

package object model {
  trait JsonProtocols extends RequestJsonProtocols with ResponseJsonProtocols
}

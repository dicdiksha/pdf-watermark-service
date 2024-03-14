package model

import play.api.libs.json.{Json, OFormat}

package object response {
  trait ResponseJsonProtocols {
    implicit val waterMarkResponseFmt: OFormat[WaterMarkResponse] = Json.format[WaterMarkResponse]
  }
}

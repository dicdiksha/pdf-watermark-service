package model

import play.api.libs.json.{Json, OFormat}

package object request {
  trait RequestJsonProtocols {
    implicit val waterMarkRequestFmt: OFormat[WaterMarkRequest] = Json.format[WaterMarkRequest]
  }
}

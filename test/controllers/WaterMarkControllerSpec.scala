package controllers

import org.scalatestplus.mockito.MockitoSugar.mock
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.WSClient
import play.api.test.Helpers._
import play.api.test._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 *
 * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
 */
class WaterMarkControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "WaterMarkController " should {
    "POST request to get  the path for Watermark PDF" in {
      val requestBody: JsValue = Json.parse(
        """
          |{
          |    "filePath":"https://obj.diksha.gov.in/dev-contents-storage/content/assets/do_113837480233238528156/busra.pdf",
          |    "mimeType":"pdf"
          |}
          |""".stripMargin)

      val request = FakeRequest(POST, "/content/pdf/v1/create/do_113837480233238528156").withJsonBody(requestBody)

      val response = route(app, request).get
      status(response) mustBe OK
    }
  }

}

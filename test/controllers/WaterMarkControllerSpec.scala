package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json.{JsValue, Json}
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

    "render the index page from a new instance of controller" in {
      val controller = new WaterMarkController(stubControllerComponents())
      val home = controller.index().apply(FakeRequest(GET, "/"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include("Welcome to Play")
    }

    "render the index page from the application" in {
      val controller = inject[WaterMarkController]
      val home = controller.index().apply(FakeRequest(GET, "/"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include("Welcome to Play")
    }

    "render the index page from the router" in {
      val request = FakeRequest(GET, "/")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include("Welcome to Play")
    }

    "POST request to get  the path for Watermark PDF" in {
      val requestBody: JsValue = Json.parse(
        """
          |{
          |    "filePath":"D:\\pdftestfile/busy-month.pdf",
          |    "mimeType":"pdf"
          |}
          |""".stripMargin)

      val request = FakeRequest(POST, "/content/pdf/v1/create").withJsonBody(requestBody)

      val response = route(app, request).get
      status(response) mustBe OK
    }
  }

}

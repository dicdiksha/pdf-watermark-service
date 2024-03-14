package service

import model.response.WaterMarkResponse
import java.io.File
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDType1Font

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class WaterMarkService {

  def addWatermark(inputPath: String,
                   outputPath: String,
                   watermarkText: String): Future[WaterMarkResponse] = Future {
    val document = PDDocument.load(new File(inputPath))
    // Define the font and size for the watermark
    val font = PDType1Font.HELVETICA_BOLD
    val fontSize = 48
    // Iterate through each page in the PDF
    for (pageIndex <- 0 until document.getNumberOfPages) {
      val page = document.getPage(pageIndex).asInstanceOf[PDPage]
      val mediaBox = page.getMediaBox
      val centerX = (mediaBox.getLowerLeftX + mediaBox.getUpperRightX) / 2
      val centerY = (mediaBox.getLowerLeftY + mediaBox.getUpperRightY) / 2

      val contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)

      contentStream.beginText()
      // Set font and font size for the watermark
      contentStream.setFont(PDType1Font.HELVETICA_BOLD, 50)

      // Set color for the watermark (adjust as needed)
      contentStream.setNonStrokingColor(200, 200, 200)

      // Calculate position for the watermark (center of the page)
      val textWidth = PDType1Font.HELVETICA_BOLD.getStringWidth(watermarkText) / 1000 * 36
      val textHeight = 36
      val x = centerX - textWidth / 2
      val y = centerY - textHeight / 2

      // Rotate the text (adjust the angle as needed)
      contentStream.setTextRotation(-Math.PI / 4, x, y)

      // Add the watermark text
      contentStream.showText(watermarkText)

      // Close the content stream
      contentStream.close()
    }
    // Save the watermarked PDF
    document.save(new File(outputPath))
    // Close the document
    document.close()
    WaterMarkResponse("1", outputPath, "Success")
  }

}

object WaterMarkService {
  def apply() = new WaterMarkService()
}

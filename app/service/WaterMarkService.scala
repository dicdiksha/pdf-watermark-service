package service

import model.response.WaterMarkResponse
import org.apache.pdfbox.pdmodel.{PDDocument, PDPageContentStream}
import org.apache.pdfbox.pdmodel.font.PDType1Font
import play.api.libs.ws.WSClient
import storage.OCIS3StorageService
import util.{AppConf, Constants}

import java.io.{ByteArrayInputStream, File, FileNotFoundException, InputStream}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class WaterMarkService(ociS3StorageService: OCIS3StorageService) {

  private def getInputStreamFromUrl(path: String)(implicit ws: WSClient): Future[ByteArrayInputStream] = {
    ws.url(path).get().map(res =>
      if (res.status == 200) {
        new ByteArrayInputStream(res.bodyAsBytes.toArray)
      } else throw new FileNotFoundException(s"File not found at Path: $path")
    )
  }

  private def uploadFile(objectKey: String, filePath: File, container: String): Future[Unit] = Future {
    ociS3StorageService.putBlob(objectKey, filePath, container)
    if(filePath.exists()){
      filePath.delete()
    }
  }

  private def addWatermark(inputStream: InputStream,
                           filePath: String,
                           watermarkText: String): Future[File] = Future {
    val tempFile: File = File.createTempFile(filePath.replaceAll(".pdf", ""), ".pdf")
    val document = PDDocument.load(inputStream)
    // Define the font and size for the watermark
    val font = PDType1Font.HELVETICA_BOLD
    val fontSize = 50
    // Iterate through each page in the PDF
    for (pageIndex <- 0 until document.getNumberOfPages) {
      val page = document.getPage(pageIndex)
      val mediaBox = page.getMediaBox
      val centerX = (mediaBox.getLowerLeftX + mediaBox.getUpperRightX) / 2
      val centerY = (mediaBox.getLowerLeftY + mediaBox.getUpperRightY) / 2

      val contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)

      contentStream.beginText()
      // Set font and font size for the watermark
      contentStream.setFont(font, fontSize)

      // Set color for the watermark (adjust as needed)
      contentStream.setNonStrokingColor(200/255f, 200/255f, 200/255f)

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
    document.save(tempFile)
    // Close the document
    document.close()
    tempFile
  }

  def addWatermark(identifier: String, inputPath: String,
                   outputPath: String,
                   watermarkText: String)(implicit ws: WSClient): Future[WaterMarkResponse] = {
    for {
      inputStream <- getInputStreamFromUrl(inputPath)
      waterMarkedFile <- addWatermark(inputStream, outputPath, watermarkText)
      _ <- uploadFile(outputPath, waterMarkedFile, AppConf.getStorageContainer)
    } yield WaterMarkResponse(identifier, Constants.PREFIXOUTPUTPATH + outputPath, Constants.SUCCESS)
  }

}

object WaterMarkService {
  def apply() = new WaterMarkService(OCIS3StorageService())
}

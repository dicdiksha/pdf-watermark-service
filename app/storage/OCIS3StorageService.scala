package storage

import com.google.common.io.Files
import com.google.common.hash
import org.jclouds.ContextBuilder
import org.jclouds.blobstore.BlobStoreContext
import util.AppConf

import java.io.File
import java.util.Properties

class OCIS3StorageService extends BaseStorage {

  val properties = new Properties()

  properties.setProperty("jclouds.provider", "s3")
  properties.setProperty("jclouds.endpoint", AppConf.getEndPoint.get)
  properties.setProperty("jclouds.s3.virtual-host-buckets", "false")
  properties.setProperty("jclouds.strip-expect-header", "true")
  properties.setProperty("jclouds.regions",AppConf.getRegion.get)
  properties.setProperty("jclouds.s3.signer-version", "4")

  val context = ContextBuilder.newBuilder("aws-s3")
    .credentials(AppConf.getStorageKey, AppConf.getStorageSecret)
    .overrides(properties)
    .buildView(classOf[BlobStoreContext])

  val blobStore = context.getBlobStore

  override def putBlob(objectKey: String, file: File, container: String): Unit = {
    val payload = Files.asByteSource(file)
    val payloadSize = payload.size()
    val payloadMD5 = payload.hash(hash.Hashing.md5())
//    val contentType = tika.detect(file)
    val blob = blobStore.blobBuilder(objectKey)
      .payload(payload)
      .contentEncoding("UTF-8")
      .contentLength(payloadSize)
      .contentMD5(payloadMD5)
      .build()

    blobStore.putBlob(container, blob)
  }

}

object OCIS3StorageService {
  def apply(): OCIS3StorageService = new OCIS3StorageService()
}

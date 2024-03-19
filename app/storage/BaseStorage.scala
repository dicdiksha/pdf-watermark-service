package storage

import java.io.File

trait BaseStorage {

  def putBlob(objectKey: String, file: File, container: String): Unit

}

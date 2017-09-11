package utils

import java.util.zip.ZipFile
import java.io.FileInputStream
import java.io.FileOutputStream
import scala.collection.JavaConversions._
import java.util.zip.ZipEntry
import java.io.InputStream
import java.io.OutputStream
import java.io.File
/**
  * Created by senthil
  */
class ZipArchive {

  val BUFSIZE = 4096
  val buffer = new Array[Byte](BUFSIZE)

  def unZip(source: String, targetFolder: String) = {
    def Desc[T : Ordering] = implicitly[Ordering[T]].reverse
    val zipFile = new ZipFile(source)

    val zipEntries = zipFile.entries.toList.sortBy(_.isDirectory)(Desc)
    zipEntries.foreach(z => println(z.getName))
    unzipAllFile(zipEntries, getZipEntryInputStream(zipFile)_, new File(targetFolder))
  }

  def getZipEntryInputStream(zipFile: ZipFile)(entry: ZipEntry) = zipFile.getInputStream(entry)

  def unzipAllFile(entryList: List[ZipEntry], inputGetter: (ZipEntry) => InputStream, targetFolder: File): Boolean = {

    entryList match {
      case entry :: entries =>

        if (entry.isDirectory)
          new File(targetFolder, entry.getName).mkdirs
        else {
          val file = new File(targetFolder, entry.getName)
          file.getParentFile.mkdirs()
          saveFile(inputGetter(entry), new FileOutputStream(file))
        }

        unzipAllFile(entries, inputGetter, targetFolder)
      case _ =>
        true
    }

  }

  def saveFile(fis: InputStream, fos: OutputStream) = {
    writeToFile(bufferReader(fis)_, fos)
    fis.close
    fos.close
  }

  def bufferReader(fis: InputStream)(buffer: Array[Byte]) = (fis.read(buffer), buffer)

  def writeToFile(reader: (Array[Byte]) => Tuple2[Int, Array[Byte]], fos: OutputStream): Boolean = {
    val (length, data) = reader(buffer)
    if (length >= 0) {
      fos.write(data, 0, length)
      writeToFile(reader, fos)
    } else
      true
  }
}

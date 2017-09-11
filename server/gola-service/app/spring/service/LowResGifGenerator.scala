package spring.service

import java.io.{BufferedOutputStream, File, FileOutputStream}
import java.nio.file.{Files, Paths}

import sys.process._
import org.im4java.core.{ConvertCmd, IMOperation}
import org.slf4j.{Logger, LoggerFactory}

import scala.io.Source

/**
  * Created by senthil
  */
class LowResGifGenerator(compressCommand: String) {

  val log: Logger = LoggerFactory.getLogger(this.getClass)

  def produceLowGif(bytes: Array[Byte]):Array[Byte]={
    val file  = File.createTempFile(System.nanoTime().toString, "orig")
    val bos = new BufferedOutputStream(new FileOutputStream(file))
    bos.write(bytes)
    bos.flush()
    bos.close()

    val lowResCommand = compressCommand + " " + file.getAbsolutePath + " " + file.getAbsolutePath
    log.info("Running gif compression command {}", lowResCommand)
    //val result:Int = lowResCommand ! ProcessLogger(stdout append _,stderr append _)
    //println(stdout)

    val result = lowResCommand !!

    print(result)

    val compressedBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath))
    try {
      file.delete()
    }catch {
      case e:Exception => log.warn("Error deleting low res file" + file.getAbsolutePath, e)
    }
    if(compressedBytes.length == bytes.length){
      log.info("No compression of gif, hence returning empty array")
      Array[Byte]()
    }else{
      compressedBytes
    }

  }
}

class WaterMarkGenerator(waterMarkCommand: String){

  val gravities = Array("NorthWest", "North", "NorthEast", "West", "Center", "East", "SouthWest", "South", "SouthEast")

  val log: Logger = LoggerFactory.getLogger(this.getClass)

  def randomGravity = {
    val index = System.currentTimeMillis() % gravities.length
    gravities(index.toInt)
  }

  def produceWaterMarkGif(bytes: Array[Byte]):Array[Byte]={
    val file  = File.createTempFile(System.nanoTime().toString, "orig")

    val resultFile  = File.createTempFile(System.nanoTime().toString, "result" + ".gif")
    val fileOutputStream = new FileOutputStream(file)
    val bos = new BufferedOutputStream(fileOutputStream)
    bos.write(bytes)
    bos.flush()
    bos.close()
    fileOutputStream.close()

    val waterMarkCmd = waterMarkCommand + " " + file.getAbsolutePath + " " + randomGravity + " " + resultFile.getAbsolutePath

    log.info("Running gif compression command {}", waterMarkCmd)
    //val result:Int = waterMarkCmd ! ProcessLogger(stdout append _,stderr append _)
//println(stdout)

    val result = waterMarkCmd !!

    print(result)


    val waterMarkBytes = Files.readAllBytes(Paths.get(resultFile.getAbsolutePath))
    try {
      file.delete()
      resultFile.delete()
    }catch {
      case e:Exception => log.warn("Error deleting low res file" + file.getAbsolutePath, e)
    }
    if(waterMarkBytes.length == bytes.length){
      log.info("No compression of gif, hence returning empty array")
      Array[Byte]()
    }else{
      waterMarkBytes
    }

  }
}

class WebPGenerator(webPCommand: String){



  val log: Logger = LoggerFactory.getLogger(this.getClass)



  def produceWebpGif(bytes: Array[Byte]):Array[Byte]={
    val file  = File.createTempFile(System.nanoTime().toString, "orig")

    val resultFile  = File.createTempFile(System.nanoTime().toString, "result" + ".webp")
    val fileOutputStream = new FileOutputStream(file)
    val bos = new BufferedOutputStream(fileOutputStream)
    bos.write(bytes)
    bos.flush()
    bos.close()
    fileOutputStream.close()

    val waterMarkExecCmd = webPCommand + " " + file.getAbsolutePath + " " + resultFile.getAbsolutePath

    log.info("Running gif compression command {}", waterMarkExecCmd)
    val result:Int = waterMarkExecCmd !


    val webPBytes = Files.readAllBytes(Paths.get(resultFile.getAbsolutePath))
    try {
      file.delete()
      resultFile.delete()
    }catch {
      case e:Exception => log.warn("Error deleting low res file" + file.getAbsolutePath, e)
    }
    if(webPBytes.length == bytes.length){
      log.info("No compression of gif, hence returning empty array")
      Array[Byte]()
    }else{
      webPBytes
    }

  }
}

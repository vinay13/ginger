package actors

import java.io.File
import java.net.URL
import java.nio.file.{Files, Paths}
import java.security.MessageDigest
import java.util

import akka.actor.ActorSystem
import akka.actor.{Actor, Props}
import akka.stream.Materializer
import utils.S3Uploader
import exception.GifAlreadyExistsException
import model.{Idiom, TumblrImageMetaData}
import org.apache.commons.io.FileUtils
import org.apache.http.impl.client.HttpClientBuilder
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.http._
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.http.converter.ByteArrayHttpMessageConverter
import org.springframework.web.client.RestTemplate
import play.api.Configuration
import play.api.libs.ws.WSClient
import springconfig.context.SpringAppContext
import scala.sys.process._
import scala.collection.JavaConverters._
import scala.reflect.io.ZipArchive

import utils.ZipArchive

/**
  * Created by senthil
  */

class NewGifActor(config: Configuration, actorSystem: ActorSystem, wsClient: WSClient, implicit val mat: Materializer) extends Actor {

  val gifService = SpringAppContext.getGifService

  val bulkUploadService = SpringAppContext.getBulkUploadService

  val userService = SpringAppContext.getUserService

  val s3Uplaoder = new S3Uploader(config.getConfig("ginger.s3").get.underlying)

  val cloudFrontUrl = config.getString("ginger.cf-url").get

  val lowResGifActor = actorSystem.actorOf(LowResGifActor.props(config, wsClient, mat))

  val log: Logger = LoggerFactory.getLogger(this.getClass)

  override def receive = {
    case NewGif(id, path, idioms, requestedBy, source, tags) => {
      log.info(s"NewGif Request for path ${path}")
      val bytes = Files.readAllBytes(Paths.get(path))
      try {
        val gif = gifService.processGifUploadRequest(requestedBy, bytes, Nil, tags, idioms.map(_.toString),
          userService, s3Uplaoder, cloudFrontUrl, lowResGifActor, true, source)
        bulkUploadService.markAsCompleted(id)
      } catch {
        case e: GifAlreadyExistsException => {
          bulkUploadService.markAsConflict(id)
        }

        case e: Exception => {
          log.info("Error when saving new gif",e)
          bulkUploadService.markAsFailed(id)
        }
      }
    }

    case UpdateMetaData(item) => {
      val id = item.getId
      try {
        updateTagsFromFile(item)

        bulkUploadService.markTumblrAsCompleted(id)
      } catch {
        case e: GifAlreadyExistsException => {
          bulkUploadService.markTumblrAsConflict(id)
        }

        case e: Exception => {
          bulkUploadService.markTumblrAsFailed(id)
        }
      }
    }

    case UpdateMetaDataForPath(path) => {

      try {
        updateTagsForPath(path)

        //bulkUploadService.markTumblrAsCompleted(id)
      } catch {
        case e: Exception => e.printStackTrace()
      }
    }

    case SuperBulkTagUpdate(path) => {


      try {
        val files = FileUtils.listFiles(
          new File(path),
          Array("zip"),
          true
        );
        files.asScala.foreach(file => {
          val metdataFilePath = file.getAbsolutePath.replace(".tumblr.com.zip", "") + "/images.txt"
          val unzipCommand:String = "unzip -d " + file.getParentFile.getAbsolutePath + " " + file.getAbsolutePath
          //new ZipArchive().unZip(file.getAbsolutePath, file.getParentFile.getAbsolutePath)
          val result:Int = unzipCommand !

          log.info(s"processing zip ${unzipCommand} result ${result}")
          bulkUploadService.saveMetaDataFromPath(metdataFilePath)
          updateTagsForPath(file.getParentFile.getAbsolutePath + "/" + file.getName.replace(".zip", ""))

        })



        //bulkUploadService.markTumblrAsCompleted(id)
      } catch {
        case e: Exception => e.printStackTrace()
      }
    }


  }

  def downloadImage(url: String) = {
    log.info(s"downloading image from ${url}")
    val httpClient = HttpClientBuilder
      .create()
      .build();
    val factory = new HttpComponentsClientHttpRequestFactory(httpClient);

    val restTemplate = new RestTemplate(factory);
    //images = ImageData(url, true, img.element, false) :: images;
    restTemplate.getMessageConverters().add(
      new ByteArrayHttpMessageConverter());

    val headers = new HttpHeaders();
    headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")

    headers.setAccept(util.Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));

    val entity = new HttpEntity[String](headers);
    try {

      var idx = url.indexOf("#")
      if (idx == -1) idx = url.indexOf("?")
      val justUrl = if (idx != -1) url.substring(0, idx) else url
      val dotidx = justUrl.lastIndexOf(".")
      val extn = justUrl.substring(dotidx)
      val response = restTemplate.exchange(
        url, HttpMethod.GET, entity, classOf[Array[Byte]]);

      if (HttpStatus.OK == response.getStatusCode) {
        log.info(s"Downloaded ${url} successfully")
        response.getBody
      } else {
        log.info(s"Download failed ${url}")
        throw new RuntimeException("Error getting the image")
      }
    } catch {
      case e: Exception => {
        e.printStackTrace()
        throw new RuntimeException("Error getting the image", e)
      }
    }
  }

  def updateTagsFromFile(item: TumblrImageMetaData) = {
    log.info(s"Processing file ${item.getPhotoUrl}")
    if (item.getPhotoUrl.endsWith(".gif")) {
      val bytes = downloadImage(item.getPhotoUrl)
      val checksum = MessageDigest.getInstance("SHA-256").digest(bytes)
      val computedSha256 = checksum.map("%02X" format _).mkString

      val gif = gifService.findBySha256(computedSha256)
      if (gif != null) {
        log.info(s"gif already exsts ${item.getPhotoUrl}, updating metadata")
        gif.getTags.addAll(item.getTags)
        gif.setTags(gif.getTags.asScala.toSet.toList.asJava)
        gif.setSource(new URL(item.getBlogUrl).getHost)
        gifService.saveGifMetaData(gif)
      } else {
        log.info(s"Processing New gif ${item.getPhotoUrl}")
        gifService.processGifUploadRequest(item.getRequestedBy, bytes, Nil, item.getTags.asScala.toList, item.getIdioms.asScala.map(_.toString).toList, userService, s3Uplaoder, cloudFrontUrl, lowResGifActor, true, new URL(item.getBlogUrl).getHost)
      }

    }
  }

  def updateTagsForPath(path: String) = {
    log.info(s"Processing file ${path}")
    val files = FileUtils.listFiles(
      new File(path),
      Array("gif"),
      true
    );

    files.asScala.foreach(file => {
      val bytes = Files.readAllBytes(Paths.get(file.getAbsolutePath))
      val checksum = MessageDigest.getInstance("SHA-256").digest(bytes)
      val computedSha256 = checksum.map("%02X" format _).mkString

      val gif = gifService.findBySha256(computedSha256)

      if (gif != null) {
        val tumblrMetaData = bulkUploadService.findMetaDataByFilePathContaining(file.getAbsolutePath)
        if(tumblrMetaData != null) {
          gif.getTags.addAll(tumblrMetaData.getTags)
          gif.setTags(gif.getTags.asScala.toSet.toList.asJava)
          gif.setSource(new URL(tumblrMetaData.getBlogUrl).getHost)
          gifService.saveGifMetaData(gif)
        }else{
          log.info(s"Tumblr metadata not found with path ${file.getAbsolutePath}")
        }

      }

    })
    new File(path).delete()
  }
}

    case class NewGif(id: String, path: String, idioms: List[Idiom], requestedBy: String, source: String, tags: List[String]) {
      def this(id: String, path: String, idioms: List[Idiom], requestedBy: String, source: String) = {
        this(id, path, idioms, requestedBy, source, Nil)
      }
    }

    case class UpdateMetaData(item: TumblrImageMetaData)

    case class UpdateMetaDataForPath(path: String)

    case class SuperBulkTagUpdate(path: String)
    object NewGifActor {
      def props(config: Configuration, actorSystem: ActorSystem, wSClient: WSClient, mat: Materializer) = Props(classOf[NewGifActor], config, actorSystem, wSClient, mat)
    }




package actors

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, File}
import java.util
import java.util.{Base64, Date}

import akka.actor.{Actor, Props}
import akka.stream.scaladsl.Sink
import akka.util.ByteString
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.S3ObjectSummary
import utils.{CommonUtils, S3Uploader}
import com.typesafe.config.Config
import model.{GifConversion, GifMetaData, LowResGifGenerationFailure, User}
import play.api.Configuration
import play.api.libs.ws.{StreamedResponse, WSClient}
import reactivemongo.bson.BSONObjectID
import spring.service.{LowResGifGenerator, WaterMarkGenerator, WebPGenerator}
import springconfig.context.SpringAppContext

import collection.JavaConverters._
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import akka.stream.Materializer
import com.amazonaws.util.IOUtils
import dto.GifConversionStatus
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.duration.Duration

/**
  * Created by senthil
  */
class LowResGifActor(config: Configuration, wsClient: WSClient, implicit val mat: Materializer) extends Actor {

  val gifService = SpringAppContext.getGifService

  val userService = SpringAppContext.getUserService

  val tabService = SpringAppContext.getTabService


  val lowResGifGenerator = new LowResGifGenerator(config.getString("ginger.lowResGifCommand").get)

  val waterMarkGifGenerator = new WaterMarkGenerator(config.getString("ginger.waterMarkCommand").get)

  val webPGenerator = new WebPGenerator(config.getString("ginger.webPCommand").get)

  val s3Uplaoder = new S3Uploader(config.getConfig("ginger.s3").get.underlying)

  val cloudFrontUrl = config.getString("ginger.cf-url").get

  val log: Logger = LoggerFactory.getLogger(this.getClass)

  //type ImageTransformer = Function1[Array[Byte], Array[Byte]]

  val lowResTransformer = lowResGifGenerator.produceLowGif _

  val webPTransformer = webPGenerator.produceWebpGif _

  override def receive = {
    case GenerateLowResGif(gifId) => {
      log.info(s"GenerateLowResGif Request for id ${gifId}")
      processLowResGifRequest(gifId)
    }

    case GenerateWaterMarkedGif(gifId) => {
      log.info(s"GenerateWaterMarkedGif Request for id ${gifId}")

      processWaterMarkRequest(gifId)
    }

    case GenerateLowResWebp(gifId) => {
      log.info(s"GenerateLowResWebp Request for id ${gifId}")

      processLowResGifWebpRequest(gifId)
    }

    case GenerateWebpTrigger() => {
      log.info("Low Res Webp Trigger")
      gifService.findBylowResUrlWebpByPendingStatus().asScala.foreach(gifConversion => {
        //if (!gifService.hasFailureForGif(gifConversion.getGifId))
        log.info(s"Low Res Webp Trigger for ${gifConversion.getGifId}")
        self ! GenerateLowResWebp(gifConversion.getGifId)
      })
    }

    case GenerateLowResTrigger() => {
      /* gifService.findGifWithoutLowRes().asScala.foreach(gif =>
         if (!gifService.hasFailureForGif(gif.getId))
           self ! GenerateLowResGif(gif.getId)
       )*/
      gifService.findBylowResUrlByPendingStatus().asScala.foreach(gifConversion => {
        //if (!gifService.hasFailureForGif(gifConversion.getGifId))
        self ! GenerateLowResGif(gifConversion.getGifId)
      })
    }

    case GenerateWaterMarkedGifTrigger() => {
      /*gifService.findGifWithoutWaterMark().asScala.foreach(gif =>
          self ! GenerateWaterMarkedGif(gif.getId)
      )*/

      gifService.findByWaterMarkedUrlPendingStatus().asScala.foreach(gifConversion => {
        self ! GenerateWaterMarkedGif(gifConversion.getGifId)
      })
    }


  }

  private def processWaterMarkRequest(gifId: String) = {
    val gifMetaData = gifService.findGifById(gifId)
    if (!gifMetaData.isWaterMarked) {
      log.info(s"GenerateLowResGif gieMetaData is ${gifMetaData}")
      val user = userService.findUserByEmailId(gifMetaData.getPublishedBy)
      val objectKey = CommonUtils.getObjectkey(gifMetaData.getBaseUrl, gifMetaData.getOriginalFN)
      log.info(s"cloudfront url is ${cloudFrontUrl}")
      log.info(s"s3 object key is ${objectKey}")
      val bytes = IOUtils.toByteArray(s3Uplaoder.getFile(objectKey))
      waterMark(gifMetaData, user, bytes)
    }
  }

  private def processLowResGifRequest(gifId: String) = {
    val gifMetaData = gifService.findGifById(gifId)
    log.info(s"GenerateLowResGif gieMetaData is ${gifMetaData}")
    val user = userService.findUserByEmailId(gifMetaData.getPublishedBy)

      /*val futureResponse = wsClient.url(gifMetaData.getUrl).withMethod("GET").stream()
      val bytes: Array[Byte] = Await.result(downloadGifBytes(futureResponse), Duration.Inf)*/
      val objectKey = CommonUtils.getObjectkey(gifMetaData.getBaseUrl, gifMetaData.getOriginalFN)
      log.info(s"cloudfront url is ${cloudFrontUrl}")
      log.info(s"s3 object key is ${objectKey}")

      val bytes = IOUtils.toByteArray(s3Uplaoder.getFile(objectKey))
      generateLowResImages(gifMetaData, user, bytes)
      if (!gifMetaData.isWaterMarked) {
        waterMark(gifMetaData, user, bytes)
      }

  }

  private def processLowResGifWebpRequest(gifId: String) = {
    val gifMetaData = gifService.findGifById(gifId)
    log.info(s"GenerateLowReswebpGif gieMetaData is ${gifMetaData}")
    val user = userService.findUserByEmailId(gifMetaData.getPublishedBy)
    if (gifMetaData.hasLowRes) {
      /*val futureResponse = wsClient.url(gifMetaData.getUrl).withMethod("GET").stream()
      val bytes: Array[Byte] = Await.result(downloadGifBytes(futureResponse), Duration.Inf)*/
      val objectKey = CommonUtils.getObjectkey(gifMetaData.getBaseUrl, gifMetaData.getLowResFN)
      log.info(s"cloudfront url is ${cloudFrontUrl}")
      log.info(s"s3 object key is ${objectKey}")

      val bytes = IOUtils.toByteArray(s3Uplaoder.getFile(objectKey))

      val webPBytes = webPTransformer(bytes)
      if (webPBytes.length > 0) {
        val webpId: String = BSONObjectID.generate().stringify
        val lowResWebpFN = if(gifMetaData.hasLowResWebp()) gifMetaData.getLowResWebpFN else "webp_lowres_" + webpId + ".webp"
        val webPFilePath = Base64.getEncoder.encodeToString(user.get.getPartnerId.getBytes) + "/" + lowResWebpFN

        s3Uplaoder.putFile(webPBytes, webPFilePath);
        gifService.updateLowResWebpUrl(gifMetaData.getId, lowResWebpFN)

        tabService.updateCuratedTabTopItems(gifMetaData.getId)

        log.info(s"GenerateLowReswebpGif completed ${gifMetaData}")
      } else {
        gifService.updateLowResWebpGifConversionStatus(gifId, GifConversionStatus.Failed)
      }
      //generateLowResImages(gifMetaData, user, bytes)

    }
  }

  private def processLowResGifRequest(gifId: String, bytes: Array[Byte]) = {
    val gifMetaData = gifService.findGifById(gifId)
    val user = userService.findUserByEmailId(gifMetaData.getPublishedBy)
    if (!gifMetaData.hasLowRes) {
      Future.successful(bytes).onSuccess { case bytes: Array[Byte] => generateLowResImages(gifMetaData, user, bytes) }
    }
  }

  private def downloadGifBytes(futureResponse: Future[StreamedResponse]) = {
    val downloadedFile: Future[Array[Byte]] = futureResponse.flatMap {
      res =>
        val outputStream = new ByteArrayOutputStream()

        // The sink that writes to the output stream
        val sink = Sink.foreach[ByteString] { bytes =>
          outputStream.write(bytes.toArray)
        }

        // materialize and run the stream
        res.body.runWith(sink).andThen {
          case result =>
            // Close the output stream whether there was an error or not
            outputStream.close()
            // Get the result or rethrow the error
            result.get
        }.map(_ => outputStream.toByteArray)
    }
    downloadedFile
  }

  private def generateLowResImages(gifMetaData: GifMetaData, user: Option[User], gifBytes: Array[Byte]) = {
    try {
      val lowResBytes = lowResTransformer(gifBytes)
      val webPBytes = webPTransformer(lowResBytes)
      if (lowResBytes.length < gifBytes.length && lowResBytes.length > 0) {
        updateLowResDetails(gifMetaData, user, lowResBytes, webPBytes)
      } else {
        /*val lowResGenerationFailure = new LowResGifGenerationFailure()
      lowResGenerationFailure.setGifId(gifMetaData.getId)
      lowResGenerationFailure.setAttemptedDate(new Date());
      gifService.save(lowResGenerationFailure)*/
        gifService.updateLowResGifConversionStatus(gifMetaData.getId, GifConversionStatus.Failed)
      }
    }catch{
      case e:Exception => {
        log.error(s"Exception lowresgif conversion ${gifMetaData.getId}", e);
        gifService.updateLowResGifConversionStatus(gifMetaData.getId, GifConversionStatus.Failed)
        gifService.updateLowResWebpGifConversionStatus(gifMetaData.getId, GifConversionStatus.Failed)
      }
    }
    //waterMark(gifMetaData, user, gifBytes)
  }


  private def updateLowResDetails(gifMetaData: GifMetaData, user: Option[User], lowResBytes: Array[Byte], webPBytes: Array[Byte]) = {
    var lowResFN:String = null;
    var lowResWebpFN:String = null

    val lowResuuid: String = BSONObjectID.generate().stringify

    if (gifMetaData.hasLowRes)
      lowResFN = gifMetaData.getLowResFN
    else
      lowResFN = "lowres_" + lowResuuid + ".gif"


    if (gifMetaData.hasLowResWebp)
      lowResWebpFN = gifMetaData.getLowResWebpFN
    else
      lowResWebpFN = "webp_lowres_" + lowResuuid + ".webp"

    log.info(s"uploading lowres for ${gifMetaData.getId}")
    val lowResFilePath = Base64.getEncoder.encodeToString(user.get.getPartnerId.getBytes) + "/" + lowResFN
    s3Uplaoder.putFile(lowResBytes, lowResFilePath);
    log.info(s"lowres upload completed for ${gifMetaData.getId}")
    val webPFilePath = Base64.getEncoder.encodeToString(user.get.getPartnerId.getBytes) + "/" + lowResWebpFN
    s3Uplaoder.putFile(webPBytes, webPFilePath);
    log.info(s"lowreswebp upload completed for ${gifMetaData.getId}")
    try {
      val image = javax.imageio.ImageIO.read(new ByteArrayInputStream(lowResBytes))
      gifService.updateLowResData(gifMetaData.getId, image.getWidth, image.getHeight, lowResBytes.length, lowResFN)
    }catch{
      case e:Exception => {
        log.error("Exception when updating gif metadata ",e)
        if(gifMetaData.getSize > 1024* 1024)
          gifService.updateLowResData(gifMetaData.getId, (gifMetaData.getWidth * .7).asInstanceOf[Int] , (gifMetaData.getHeight * .7).asInstanceOf[Int], lowResBytes.length, lowResFN)
        else
          gifService.updateLowResData(gifMetaData.getId, gifMetaData.getWidth, gifMetaData.getHeight, lowResBytes.length, lowResFN)
      }
    }


    gifService.updateLowResWebpUrl(gifMetaData.getId, lowResWebpFN)
    log.info(s"file name updated for ${gifMetaData.getId}")
    tabService.updateCuratedTabTopItems(gifMetaData.getId)

  }

  private def waterMark(gifMetaData: GifMetaData, user: Option[User], gifBytes: Array[Byte]) = {
    try {
      val waterMarkBytes = waterMarkGifGenerator.produceWaterMarkGif(gifBytes)
      if (gifBytes.length != waterMarkBytes.length && waterMarkBytes.length > 0) {

        val waterMarkUuid: String = BSONObjectID.generate().stringify
        var waterMarkedFileName = ""
        if (gifMetaData.isWaterMarked)
          waterMarkedFileName = gifMetaData.getWaterMarkedFN;
        else
          waterMarkedFileName = "watermarked_" + waterMarkUuid + ".gif"

        val waterMarkFilePath = Base64.getEncoder.encodeToString(user.get.getPartnerId.getBytes) + "/" + waterMarkedFileName
        s3Uplaoder.putFile(waterMarkBytes, waterMarkFilePath);
        val waterMarkedUrl = cloudFrontUrl + "/" + waterMarkFilePath

        gifMetaData.setWaterMarkedFN(waterMarkedFileName)
        gifService.updateWaterMarkedUrl(gifMetaData.getId, waterMarkedFileName)

        tabService.updateCuratedTabTopItems(gifMetaData.getId)
        //gifService.updateWaterMakrStatus(gifMetaData.getId, GifConversionStatus.Success)
      } else {
        gifService.updateWaterMakrStatus(gifMetaData.getId, GifConversionStatus.Failed)
      }
    }catch{
      case e:Exception => {
        log.error(s"Exception Watermarking gif ${gifMetaData.getId}", e);
        gifService.updateWaterMakrStatus(gifMetaData.getId, GifConversionStatus.Failed)
      }
    }
  }
}


case class GenerateLowResGif(gifId: String)

case class GenerateLowResWebp(gifId: String)


case class GenerateWaterMarkedGif(gifId: String)

case class GenerateLowResGifFromBytes(gifId: String, bytes: Array[Byte])

case class GenerateLowResTrigger()

case class GenerateWaterMarkedGifTrigger()

case class GenerateWebpTrigger()


object LowResGifActor {
  def props(config: Configuration, wSClient: WSClient, mat: Materializer) = Props(classOf[LowResGifActor], config, wSClient, mat)
}



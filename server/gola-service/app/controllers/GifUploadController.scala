package controllers

import java.io._
import java.nio.file.Files
import java.nio.file.attribute.{FileAttribute, PosixFilePermissions}
import java.security.MessageDigest
import java.util
import util.{Base64, Date}
import javassist.bytecode.ByteArray
import javax.imageio.ImageIO
import javax.inject.{Inject, Singleton}

import actors._
import akka.actor.ActorSystem
import akka.stream.Materializer
import com.amazonaws.services.s3.{AmazonS3, AmazonS3Client}
import com.amazonaws.services.s3.model.{DeleteObjectsRequest, ListObjectsRequest, S3ObjectSummary}
import com.google.gson.Gson
import utils.S3Uploader
import dao.{GifMetaDataDao, UserDao}
import dto.DtoSerializers._
import dto.{Result => _, _}
import exception.GifAlreadyExistsException
import model._
import org.apache.commons.io.FileUtils
import org.apache.commons.io.filefilter.{DirectoryFileFilter, RegexFileFilter}
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.http.{HttpEntity, HttpHeaders, HttpMethod, MediaType}
import org.springframework.http.converter.ByteArrayHttpMessageConverter
import org.springframework.web.client.RestTemplate
import play.api.Configuration
import play.api.libs.Files.TemporaryFile
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import play.api.mvc.MultipartFormData.FilePart
import play.api.mvc._
import play.core.parsers.Multipart
import predef.Generator._
import reactivemongo.bson.BSONObjectID
import spring.service.{LowResGifGenerator, TabService, UserService}
import springconfig.context.SpringAppContext
import utils.{GsonConfig, ZipArchive}

import scala.util.{Failure, Success}
import collection.JavaConverters._
import scala.collection.mutable
import scala.io.Source
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by senthil
  */


@Singleton
class GifUploadController @Inject()(config: Configuration, system: ActorSystem, wsClient: WSClient, implicit val mat: Materializer) extends Controller with GingerActionBuilders {

  val gifService = SpringAppContext.getGifService

  val userService = SpringAppContext.getUserService

  val bulkUploadService = SpringAppContext.getBulkUploadService

  val tabService: TabService = SpringAppContext.getTabService


  val s3Uplaoder = new S3Uploader(config.getConfig("ginger.s3").get.underlying)

  val lowResGifGenerator = new LowResGifGenerator(config.getString("ginger.lowResGifCommand").get)

  val lowResGifActor = system.actorOf(LowResGifActor.props(config, wsClient, mat))

  val newGifActor = system.actorOf(NewGifActor.props(config, system, wsClient, mat))

  val log: Logger = LoggerFactory.getLogger(this.getClass)

  override def userFinder: UserService = SpringAppContext.getUserService

  triggerGifConversions()

  private def triggerGifConversions() = {
    val conversionEnabled = config.getBoolean("ginger.gifConversionTriggerEnabled").getOrElse(false)
    log.info("Gif Conversion Config is " + conversionEnabled)
    if (conversionEnabled) {
      system.scheduler.schedule(2 minutes, config.getLong("ginger.lowResGenerationIntervalInMinutes").get minutes, lowResGifActor, GenerateLowResTrigger())

      system.scheduler.schedule(3 minutes, config.getLong("ginger.lowResGenerationIntervalInMinutes").get minutes, lowResGifActor, GenerateWaterMarkedGifTrigger())
    }
  }

  def uploadGif() = OptionalAuthenticatedAction(Multipart.multipartParser(10 * 100 * 1024, Multipart.handleFilePartAsTemporaryFile)) { implicit request =>
    val userId = request.headers.get("currentUser").getOrElse("Test")
    log.info("Upload Gif request received from {}", userId)
    request.body.file("gif").map(mp => {
      val bis = new BufferedInputStream(new FileInputStream(mp.ref.file.getAbsolutePath))
      val bArray = Stream.continually(bis.read).takeWhile(-1 !=).map(_.toByte).toArray
      val tags = request.body.dataParts.get("tags").map(_ (0).split(",").toList).getOrElse(Nil);
      val idioms = request.body.dataParts.get("idiom").map(_ (0).split(",").toList).getOrElse(Nil);
      if (idioms == Nil)
        BadRequest
      else
        try {
          val gif = gifService.processGifUploadRequest(userId, bArray, request.body.dataParts.get("categories").map(_ (0).split(",").toList).getOrElse(Nil),
            tags, idioms, userFinder, s3Uplaoder, config.getString("ginger.cf-url").get, lowResGifActor, false)
          Ok(gson.toJson(gif))
        } catch {
          case e: GifAlreadyExistsException => Conflict
        }
    }).getOrElse(BadRequest)
  }

  def bulkUploadFromLocalPath() = AdminAction { implicit request => {
    val path = request.body.asText.get
    val userId = request.headers.get("currentUser").get
    bulkUploadService.saveBulkUpload(path, userId, newGifActor)
    Ok
  }

  }

  def bulkUploadFromTumblrMetaData() = AdminAction { implicit request => {
    val path = request.body.asText.get
    val userId = request.headers.get("currentUser").get
    bulkUploadService.updateTagsFromFile(path, userId, newGifActor)
    Ok
  }

  }

  def superBulkTagUpdate() = AdminAction { implicit request => {

    val path = request.body.asText.get
    val userId = request.headers.get("currentUser").get
    newGifActor ! SuperBulkTagUpdate(path)
    Ok
  }

  }

  def updateTagsUsingTumbleMetaData() = AdminAction { implicit request => {
    val path = request.body.asText.get
    val userId = request.headers.get("currentUser").get
    bulkUploadService.updateTagsFromMetaData(path, userId, newGifActor)
    Ok
  }

  }

  def resumeBulkUpload() = AdminAction { implicit request => {
    val path = request.body.asText.get
    val userId = request.headers.get("currentUser").get
    bulkUploadService.resume(path, userId, newGifActor)
    Ok
  }

  }

  def bulkUploadZip() = OptionalAuthenticatedAction(Multipart.multipartParser(100 * 100 * 1024, Multipart.handleFilePartAsTemporaryFile)) { implicit request =>
    val userId = request.headers.get("currentUser").getOrElse("Test")
    log.info("Upload Gif request received from {}", userId)
    val idioms = request.body.dataParts.get("idiom").map(_ (0).split(",").toList).getOrElse(Nil);
    if (idioms == Nil) BadRequest
    else {
      request.body.file("gif").map(mp => {
        val permissions: Array[FileAttribute[_]] = Array(PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwxr-x---")))
        val bulkupload: String = "bulkupload" + System.nanoTime()
        val tempDirPath = Files.createTempDirectory(bulkupload)
        new ZipArchive().unZip(mp.ref.file.getAbsolutePath, tempDirPath.toString)
        val file = tempDirPath.toFile
        val files = recursiveListFiles(file)
        val userId = request.headers.get("currentUser").getOrElse("Test")
        val result = files.map(f => {
          if (f.isFile) {
            val bis = new BufferedInputStream(new FileInputStream(f))
            if (f.getName.endsWith(".gif")) {
              val bArray = Stream.continually(bis.read).takeWhile(-1 !=).map(_.toByte).toArray
              val processResult = gifService.processGifUploadRequest(userId, bArray, Nil, Nil, idioms, userFinder, s3Uplaoder, config.getString("ginger.cf-url").get, lowResGifActor, true)
              if (processResult == Ok)
                f.getName + " uploaded successfully "
              if (processResult == Conflict)
                f.getName + " exists already"
              else {
                f.getName + " upload failed"
              }
            } else
              f.getName + " is not gif file "
          }
        })
        Ok(result.mkString("\r\n"))
      }).getOrElse(BadRequest)
    }
  }

  def uploadFromUrl() = AuthenticatedAction { implicit request =>
    val userId = request.headers.get("currentUser").get
    log.info("Upload Gif request received from {}", userId)
    val uploadRequest = gson.fromJson(request.body.asJson.get.toString(), classOf[UploadFromUrlRequest])
    val restTemplate = new RestTemplate();
    restTemplate.getMessageConverters().add(
      new ByteArrayHttpMessageConverter());

    val headers = new HttpHeaders();
    headers.setAccept(util.Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));

    val entity = new HttpEntity[String](headers);

    val response = restTemplate.exchange(
      uploadRequest.getUrl,
      HttpMethod.GET, entity, classOf[Array[Byte]]);
    try {
      val gif = gifService.processGifUploadRequest(userId, response.getBody, uploadRequest.getCategories.asScala.toList, uploadRequest.getTags.asScala.toList, uploadRequest.getIdioms.asScala.toList, userFinder, s3Uplaoder, config.getString("ginger.cf-url").get, lowResGifActor)
      Ok(gson.toJson(gif))
    } catch {
      case e: GifAlreadyExistsException => Conflict
    }
  }

  def updateTags() = AuthenticatedAction {
    implicit request =>
      def saveTags(tagReq: TagRequest, currentTags: util.List[String], user: User) = {
        val result = gifService.updateTags(tagReq.id, tagReq.tags, true, currentTags, user)
        Ok
      }

      val id: String = request.headers.get("currentUser").get
      val user = userFinder.findUserByEmailId(id).get
      val tagsRequest = Json.fromJson[TagRequest](request.body.asJson.get).asOpt
      //log.info("Update tags received for  {} by {}", tagsRequest.get.id, id)
      val result = tagsRequest.map(tagReq => {
        val gif = gifService.findGifById(tagReq.id)
        if (gif == null)
          NotFound
        else if (gif.getPublishedBy.equals(user.getEmailId) || user.getUserRoles.contains(UserRole.Admin) || user.getUserRoles.contains(UserRole.Tagger)) {
          saveTags(tagReq, gif.getTags, user)
        } else
          BadRequest
      })
      result.getOrElse(BadRequest)
  }

  def updateCategories() = AuthenticatedAction {
    implicit request =>

      def saveCategories(categoriesRequest: CategoryRequest) = {
        val result = gifService.updateCategories(categoriesRequest.id, categoriesRequest.categories)
        Ok
      }

      val user = userFinder.findUserByEmailId(request.headers.get("currentUser").get).get
      val categoriesRequestOpt = Json.fromJson[CategoryRequest](request.body.asJson.get).asOpt
      val result = categoriesRequestOpt.map(categoryRequest => {
        val gif = gifService.findGifById(categoryRequest.id)
        if (gif == null)
          NotFound
        else if (gif.getPublishedBy.equals(user.getEmailId)) {
          saveCategories(categoryRequest)
        } else
          BadRequest
      })
      result.getOrElse(BadRequest)
  }


  val gson = GsonConfig.newGson()


  private def generateAndUpdateLowResGif(user: User, bytes: Array[Byte], cloudFrontUrl: String, gifMetaData: GifMetaData) = {
    val lowResGif = lowResGifGenerator.produceLowGif(bytes)
    if (lowResGif.length < bytes.length) {
      log.info("low res compression orginal size {}, reduced size {}", bytes.length, lowResGif.size)
      val lowResuuid: String = BSONObjectID.generate().stringify
      val lowResFN = lowResuuid + ".gif"
      val lowResFilePath = Base64.getEncoder.encodeToString(user.getPartnerId.getBytes) + "/" + lowResFN
      s3Uplaoder.putFile(lowResGif, lowResFilePath);
      val lowResFullUrl = cloudFrontUrl + "/" + lowResFilePath
      gifMetaData.setLowResFN(lowResFN)
    } else {
      log.info("low res compression yielded no result for gif id {}", gifMetaData.getId)
    }
  }

  /*def bulkUpload(idiom: String) = AuthenticatedAction {
    implicit request => {

      val pathTxt = request.body.asText.get
      val permissions:Array[FileAttribute[_]] = Array(PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwxr-x---")))
      val bulkupload:String = "bulkupload"
      val tempDirPath = Files.createTempDirectory(bulkupload)
      new ZipArchive().unZip(pathTxt, tempDirPath.toString)
      val file = tempDirPath.toFile
      val files = recursiveListFiles(file)
      val userId = request.headers.get("currentUser").getOrElse("Test")
      files.foreach(f => {
        if(f.isFile) {
          val bis = new BufferedInputStream(new FileInputStream(f))
          if(f.getName.endsWith(".gif")){
            val bArray = Stream.continually(bis.read).takeWhile(-1 !=).map(_.toByte).toArray
            processGifUploadRequest(userId, bArray, Nil, getTags(f), idiom)
          }
        }
      })
    }
      Ok
  }*/

  def isGifFile(file: File) = {
    file.getName.endsWith(".gif")
  }

  def getTags(file: File) = {
    val parentTagFile = new File(file.getParentFile.getParentFile.getAbsolutePath, file.getParentFile().getName + ".txt")
    //val tagFile = new File(file.getParentFile.getAbsoluteFile, getGifTags`(file))
    getTagsFromFile(parentTagFile) ++ getGifTags(file)
  }


  private def getGifTags(file: File) = {
    val tagFiles = file.getParentFile.listFiles(new FilenameFilter {
      override def accept(dir: File, name: String) = name.endsWith(".txt")
    })
    if (tagFiles.length == 0)
      Nil
    else {
      val tagFile = tagFiles(0)
      getTagsFromFile(tagFile)
    }
  }

  private def getTagsFromFile(tagFile: File) = {
    if (tagFile.exists()) {
      val lines = for (line <- Source.fromFile(tagFile).getLines()) yield line
      lines.map(_.split("[,]").toList).flatMap(identity).toList
    } else Nil
  }

  def recursiveListFiles(f: File): Array[File] = {
    val these = f.listFiles
    these ++ these.filter(_.isDirectory).flatMap(recursiveListFiles)
  }


  def listObjectInS3() = Action {
    implicit request => {
      var count = 0;
      val existingFiles = Source.fromFile("/home/ubuntu/existingFiles").getLines().map(line => {
        line.substring(line.lastIndexOf("/") + 1)
      }).toList.sorted
      var filesMarkedForDeletion = mutable.ListBuffer[String]()
      Source.fromFile("/home/ubuntu/out").getLines().foreach(line => {
        //val gifMetaData = gifService.findGifUsingFileName(line)
        if (existingFiles.contains(line)) {
          //println(fileName +",   " + gifMetaData.getUrl + "  " + gifMetaData.getOriginalUrl + " " + gifMetaData.getThumbNailUrl + " " + gifMetaData.getLowResUrl + " " + gifMetaData.getLowResWebpUrl)
          log.info("valid file" + line)
        }
        else {
          log.info("file name is not used, hence marked for deleting - " + line)
          /*val deleteObject = new DeleteObjectsRequest("gola-gif-store-dev")
        deleteObjectKeys += obj.getKey*/
          count = count + 1
          filesMarkedForDeletion += line
        }

        if (filesMarkedForDeletion.size >= 500) {
          val deleteObjectsRequest = new DeleteObjectsRequest("gola-gif-store-dev")
          val keys = filesMarkedForDeletion.map("R2luZ2Vy/" + _)
          deleteObjectsRequest.withKeys(keys: _*)
          s3Uplaoder.s3client.deleteObjects(deleteObjectsRequest)
          filesMarkedForDeletion.clear()
        }

      })
      log.info("Files marked for deletion" + count)
    }
      Ok
  }

  def uploadInGolaFormat() = Action { implicit request =>
    val uploadRequest = gson.fromJson(request.body.asJson.get.toString(), classOf[GolaZipRequestDto])
    bulkUploadService.bulkUploadGolaZips(uploadRequest.getPath, uploadRequest.getSource, uploadRequest.getUser, newGifActor)
    Ok
  }

  def uploadTags(idiom: String, tabId: String) = AdminAction(Multipart.multipartParser(10 * 100 * 1024, Multipart.handleFilePartAsTemporaryFile)) { implicit request =>
    val userId = request.headers.get("currentUser").get
    log.info("Upload tag request received from {}", userId)
    val file = request.body.file("file")
    val path = file.get.ref.file.getAbsolutePath
    val fromFile = Source.fromFile(path)
    val src = fromFile.getLines()


    val uploadData=src.toList;

    val uploadDtos = uploadData.map(line => {
      val cols = line.split(",")
      val displayName = if(cols.length > 1) cols(1) else null
      val exclude = if(cols.length > 2) cols(2) else null
      val tagUploadDto = new TagUploadDto(cols(0),displayName, exclude);
      tagUploadDto
    }).toList

    uploadDtos.foreach(dto => {
      val textOrder = uploadDtos.indexOf(dto)
      log.info(s"Order of text is ${dto.getText} ${textOrder}")
      dto.setOrder(textOrder)
    })


    val tagArrayList=uploadDtos.map(data=>{
      data.getText
    }).toList


    if (idiom == null || tabId == null)
      BadRequest
    else
      try {

        val saveTabs:Tabs = tabService.uploadTags(Idiom.valueOf(idiom),tabId,tagArrayList);
        val tab = saveTabs.getTabs.asScala.find(_.getId.equals(tabId)).get
        tabService.saveTab(tab,Idiom.valueOf(idiom),uploadDtos)
        Ok(gson.toJson(tabService.getTabs(idiom)))
      } catch {
        case e: GifAlreadyExistsException => Conflict
      }

  }

  def triggerLowResGifGenerator()=AdminAction{

      lowResGifActor ! GenerateLowResTrigger()
    //  lowResGifActor ! GenerateWebpTrigger()
    //lowResGifActor ! GenerateWaterMarkedGifTrigger()

    Ok

  }
}
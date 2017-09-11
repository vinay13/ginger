package spring.service

import java.io.File
import java.net.URL
import java.nio.file.{Files, Paths}
import java.security.MessageDigest
import java.util
import java.util.Date
import java.util.regex.Pattern

import actors.{NewGif, UpdateMetaData, UpdateMetaDataForPath}
import akka.actor.ActorRef
import model._
import org.apache.commons.io.FileUtils
import org.apache.http.impl.client.HttpClientBuilder
import org.slf4j.LoggerFactory.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http._
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.http.converter.ByteArrayHttpMessageConverter
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import springconfig.repo.{BulkUploadRequestRepository, GolaZipGifRequestRepository, RequestedFileRepository, TumblrImageMetaDataRepository}
import utils.TumleThreeMetaDataParser

import scala.collection.JavaConverters._
import scala.io.Source

/**
  * Created by senthil
  */

@Service
class BulkUploadService(@Autowired bulkUploadRequestRepository: BulkUploadRequestRepository,
                        @Autowired requestRepository: RequestedFileRepository,
                        @Autowired tumlrImgMetaRepo: TumblrImageMetaDataRepository,
                        @Autowired golaZipGifRequestRepository: GolaZipGifRequestRepository,
                        @Autowired gifService: GifService) {

  private val logger = getLogger(classOf[BulkUploadService])

  def saveBulkUpload(path: String, user: String, newGifActor: ActorRef) = {
    val file = new File(path)
    val files = FileUtils.listFiles(
      file,
      Array("gif"),
      true
    );
    val parentFolder = file.getParent
    val txtFile = new File(parentFolder, file.getName + ".txt")
    val idioms = new String(Files.readAllLines(Paths.get(txtFile.getAbsolutePath)).get(0)).split("[,]").map(lang => {
      logger.info(s"language is ${lang}")
      logger.info(s"language idiom is ${Idiom.valueOf(lang)}")
      Idiom.valueOf(lang)
    })

    logger.info(s"received files path ${path} gifs ${files}")


    var bulkUploadRequest = new BulkUploadRequest()
    bulkUploadRequest.setPath(path)


    val filePaths = files.asScala.map(f => new RequestedFile(f.getAbsolutePath, UploadRequestStatus.Pending, new Date(), findSourceFromPath(f.getAbsolutePath).getOrElse("Gola"))).toList.asJava

    bulkUploadRequest.setRequestedBy(user)
    bulkUploadRequest.setIdioms(idioms.toList.asJava)
    val savedFiles = requestRepository.save(filePaths)
    bulkUploadRequest.setFiles(savedFiles)
    bulkUploadRequest = bulkUploadRequestRepository.save(bulkUploadRequest)
    bulkUploadRequest.getFiles.asScala.foreach(f => newGifActor ! NewGif(f.getId, f.getPath, bulkUploadRequest.getIdioms.asScala.toList, user, f.getSource, Nil))

  }


  def resume(path: String, user: String, newGifActor: ActorRef) = {
    val bulkUploadRequest = bulkUploadRequestRepository.findOne(path)
    val filePaths = bulkUploadRequest.getFiles.asScala.filter(_.getUploadRequestStatus == UploadRequestStatus.Pending).foreach(f => {
      bulkUploadRequest.getFiles.asScala.foreach(f => newGifActor ! NewGif(f.getId, f.getPath, bulkUploadRequest.getIdioms.asScala.toList, user, f.getSource, Nil))
    })
  }

  def findSourceFromPath(path: String) = {
    val DOMAIN_NAME_PATTERN = "^((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\.)+[A-Za-z]{2,6}$";

    val pDomainNameOnly = Pattern.compile(DOMAIN_NAME_PATTERN);
    path.split("[/]").dropRight(1).find(pDomainNameOnly.matcher(_).matches())
  }

  def markAsCompleted(id: String) = {
    val file = requestRepository.findOne(id)
    file.setUploadRequestStatus(UploadRequestStatus.Succcessful)
    file.setCompletedDate(new Date)
    requestRepository.save(file)
  }

  def markAsConflict(id: String) = {
    val file = requestRepository.findOne(id)
    file.setUploadRequestStatus(UploadRequestStatus.FileAlreadyExists)
    file.setCompletedDate(new Date)
    requestRepository.save(file)
  }

  def markAsFailed(id: String) = {
    val file = requestRepository.findOne(id)
    file.setUploadRequestStatus(UploadRequestStatus.Failed)
    file.setCompletedDate(new Date)
    requestRepository.save(file)
  }


  def saveMetaDataFromPath(path: String) = {
    val metadataList = new TumleThreeMetaDataParser().getTumbleThreeParser(path).filter(_.getPhotoUrl.endsWith(".gif"))
    //val txtFile = new File(new File(path).getParent, new File(path).getName.replace(".metadata", "") + ".txt")
    val savedList = tumlrImgMetaRepo.save(metadataList.asJava)
  }




  def updateTagsFromFile(path: String,  user: String, newGifActor: ActorRef)={
    val metadataList = new TumleThreeMetaDataParser().getTumbleThreeParser(path + ".metadata").filter(_.getPhotoUrl.endsWith(".gif"))
    val txtFile = new File(new File(path).getParent, new File(path).getName.replace(".metadata", "") + ".txt")


    val idioms = new String(Files.readAllBytes(Paths.get(txtFile.getAbsolutePath))).split("[,]").map(Idiom.valueOf(_)).toList.asJava
    metadataList.foreach(item => {
      item.setRequestedBy(user)
      item.setRequestedDate(new Date())
      item.setIdioms(idioms)
    })
    val savedList = tumlrImgMetaRepo.save(metadataList.asJava)
    savedList.asScala.foreach(item => {
      newGifActor ! UpdateMetaData(item)

    })
  }

  def bulkUploadGolaZips(path: String, source: String, user: String, newGifActor: ActorRef)={
    val languages = Idiom.values().map(_.toString.toLowerCase)
    val file = new File(path)
    val files = FileUtils.listFiles(
      file,
      Array("gif"),
      true
    );
    val parentFolder = file.getParent
    val txtFile = new File(path, "metadata.txt")
    val gifRequests = Files.readAllLines(Paths.get(txtFile.getAbsolutePath)).asScala.map(line => {
      val parts = line.split("[,]")
      val golaGifUploadRequest = new RequestedFile()
      golaGifUploadRequest.setPath(path + "/" +  parts(0) + ".gif")
      val idioms = parts.filter( part =>  languages.contains(part.toLowerCase()) ).map(part => Idiom.valueOf(part.capitalize)).toList.asJava
      val tags = parts.tail.filterNot( part =>  languages.contains(part.toLowerCase())).filter(_.trim.length > 0).map(_.replace("\"", "")).toList.asJava
      golaGifUploadRequest.setIdioms(idioms)
      golaGifUploadRequest.setTags(tags)
      golaGifUploadRequest.setSource(source)
      requestRepository.save(golaGifUploadRequest)
    })
    gifRequests.foreach(f => newGifActor ! NewGif(f.getId, f.getPath, f.getIdioms.asScala.toList, user, f.getSource, f.getTags.asScala.toList))
  }

  def updateTagsFromMetaData(path: String,  user: String, newGifActor: ActorRef)={
    val metadataList = new TumleThreeMetaDataParser().getTumbleThreeParser(path + ".metadata").filter(_.getPhotoUrl.endsWith(".gif"))
    val txtFile = new File(new File(path).getParent, new File(path).getName.replace(".metadata", "") + ".txt")


    val idioms = new String(Files.readAllLines(Paths.get(txtFile.getAbsolutePath)).get(0)).split("[,]").map(lang => {
      logger.info(s"language is ${lang}")
      logger.info(s"language idiom is ${Idiom.valueOf(lang)}")
      Idiom.valueOf(lang)
    }).toList.asJava
    metadataList.foreach(item => {
      item.setRequestedBy(user)
      item.setRequestedDate(new Date())
      item.setIdioms(idioms)
    })
    val savedList = tumlrImgMetaRepo.save(metadataList.asJava)

    newGifActor ! UpdateMetaDataForPath(path)
  }

  def markTumblrAsCompleted(id: String) = {
    val file = tumlrImgMetaRepo.findOne(id)
    file.setUploadRequestStatus(UploadRequestStatus.Succcessful)
    file.setCompletedDate(new Date)
    tumlrImgMetaRepo.save(file)
  }

  def markTumblrAsConflict(id: String) = {
    val file = tumlrImgMetaRepo.findOne(id)
    file.setUploadRequestStatus(UploadRequestStatus.FileAlreadyExists)
    file.setCompletedDate(new Date)
    tumlrImgMetaRepo.save(file)
  }

  def markTumblrAsFailed(id: String) = {
    val file = tumlrImgMetaRepo.findOne(id)
    file.setUploadRequestStatus(UploadRequestStatus.Failed)
    file.setCompletedDate(new Date)
    tumlrImgMetaRepo.save(file)
  }


  def findMetaDataByFilePathContaining(filePath: String)={
    val path = new File(filePath).getName.replace("tumblr_", "").substring(0,17)
    val pattern = Pattern.compile(Pattern.quote(path), Pattern.CASE_INSENSITIVE)
    tumlrImgMetaRepo.findByPhotoUrlContaining(pattern)
  }
}

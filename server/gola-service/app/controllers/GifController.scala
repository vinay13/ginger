package controllers

import java.util.Base64
import javax.inject.Inject

import com.google.gson.Gson
import dto.{GifCensorRequest, GifItem, TagUpdateCountDto, UpdateIdiomRequest}
import model.{UserProfile, UserRole}
import play.api.mvc.{Action, AnyContent, Controller}
import spring.service.UserService
import springconfig.context.SpringAppContext
import utils.{GifMetaDataConverter, GsonConfig}

import scala.concurrent.Future

/**
  * Created by senthil
  */
class GifController @Inject()() extends Controller with GingerActionBuilders {

  override def userFinder: UserService = SpringAppContext.getUserService

  def gifService = SpringAppContext.getGifService

  val gson = GsonConfig.newGson()

  def censorGif() = AdminAction { implicit request => {
    val censorRequest = gson.fromJson(request.body.asJson.get.toString(), classOf[GifCensorRequest])
    gifService.enableGif(censorRequest.getGifId, censorRequest.isEnable, censorRequest.getReason, request.headers.get("currentUser").get)
    Ok
  }
  }

  def convertToNewFormat() = AdminAction {
    gifService.convertGifToNewFormat()
    Ok
  }

  def getGifById(gifId:String) = ConsumerAction { implicit request =>
    val metaData = gifService.getGifById(gifId)
    val user = userFinder.findUserByEmailId(metaData.getPublishedBy)
    userFinder.findProfileByEmailId(metaData.getPublishedBy) match{
      case Some(up) => {
        Ok(gson.toJson(GifMetaDataConverter.convertToDto(metaData, up, user)))
      }
      case None => {
        val userProfile = new UserProfile(metaData.getPublishedBy)
        Ok(gson.toJson(GifMetaDataConverter.convertToDto(metaData, userProfile, user)))
      }
    }
  //  Ok(gson.toJson(metaData))
  }

  def getGifsByIdList() = AdminAction { implicit request =>
    val gifIdList= gson.fromJson(request.body.asJson.get.toString(), classOf[GifItem]).getGifIdList
    Ok(gson.toJson(gifService.getGifByIdList(gifIdList)))
  }



    //val request = gson.fromJson(json, classOf[GifCensorRequest])



  def getAllTags() = AuthAction(UserRole.Admin :: UserRole.Tagger :: UserRole.ContentCreator :: Nil) { implicit request => {
    Ok(gson.toJson(gifService.getAllTags()))
  }
  }

  def updateIdioms(gifId: String) = AuthAction(UserRole.Admin :: UserRole.Tagger :: UserRole.ContentCreator  :: Nil) { implicit request => {
    val updateIdiomRequest = gson.fromJson(request.body.asJson.get.toString(), classOf[UpdateIdiomRequest])
    gifService.updateIdioms(gifId, updateIdiomRequest.getIdioms)
    Ok
  }
  }

  def updateGifSource(gifId: String, source: String) = AuthAction(UserRole.Admin :: Nil) { implicit request => {
    gifService.updateSource(gifId, new String(Base64.getDecoder.decode(source.getBytes)))
    Ok
  }
  }

  def updateGifTriggerData(gifId: String, triggerData: String) = AuthAction(UserRole.Admin :: Nil) { implicit request => {
    gifService.updateTriggerData(gifId, new String(Base64.getDecoder.decode(triggerData.getBytes)))
    Ok
  }
  }


  def getTrendingDetails(gifId: String) = ConsumerAction{ implicit request =>
    Ok(gson.toJson(gifService.getTrendingDetails(gifId)))
  }

  def deleteTriggerData(gifId: String) = AdminAction{ implicit request =>
    Ok(gson.toJson(gifService.deleteTriggerData(gifId)))
  }

  def saveCustomTags() = AdminAction{ implicit request =>
    Ok(gson.toJson(gifService.saveCategoriesAndTags(request.body.asText.get)))
  }

  def getCategoriesAndTags() = Action{ implicit request =>
    Ok(gson.toJson(gifService.getCategoriesAndTags()))
  }

  def getTrendingStats() = AdminAction{ implicit request =>
    Ok(gson.toJson(gifService.getGifTrendingStats()))
  }

  def getCountStats() = AdminAction{ implicit request =>
    Ok(gson.toJson(gifService.getGifCountStats()))
  }

  def getTagCount(userId:String) = AuthenticatedAction { implicit request =>
    val userId = request.headers.get("currentUser").get
    Ok(gson.toJson(new TagUpdateCountDto(gifService.getTagsUpdateCountBy(userId), userId)))

  }


  def updateGifUrlStatus() = AdminAction {
    val gifUrlStatuses = gifService.updateGifUrlStatus()
    Ok
  }

  def createSearchTagsInGifMetaData()=AdminAction {
    gifService.createSearchTagsInGifMetaData()
    Ok
  }

  def migrateBaseUrl() = AdminAction {
    gifService.migrateBaseUrl();
    Ok

  }

}

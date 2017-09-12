package controllers

import java.util.Base64
import javax.inject.Inject

import com.google.gson.Gson
import dao.GifMetaDataDao
import dto.{GifTagDetail, FavouriteGifDTO, Filter, PageResponse}
import model.GifMetaData
import org.slf4j.LoggerFactory.getLogger
import play.api.mvc.{Action, Controller, Results}
import play.mvc.Security.AuthenticatedAction
import spring.service.{GifService, UserService}
import springconfig.context.SpringAppContext
import utils.GsonConfig

import scala.collection.JavaConverters._
import scala.concurrent.Future
import scala.concurrent.duration.{Duration, TimeUnit}

/**
  * Created by senthil
  */
class GifHitController @Inject()() extends Controller with GingerActionBuilders {

  val gifService: GifService = SpringAppContext.getGifService

  override def userFinder: UserService = SpringAppContext.getUserService

  val gson = GsonConfig.newGson()

  private val logger = getLogger(classOf[GifHitController])

  def viewGif(id: String) = OptionalAuthenticatedAction {
    implicit request =>
      Ok(gson.toJson(gifService.gifViewed(id, request.headers.get("currentUser"))))
  }

  def markAsFaviourite(id: String) = AuthenticatedAction {
    implicit request =>
      gifService.markAsFaviourite(id, request.headers.get("currentUser").get)
      Ok
  }

  def getFaviourites() = AuthenticatedAction {
    implicit request =>
      val gifs = gifService.getFaviourites(request.headers.get("currentUser").get)
      logger.info(s"Favourite gif id is ${gifs.getGifs}")
      logger.info(s"Favourite gif id is ${gifs.getGifs.keySet()}")
      Ok(gson.toJson(new FavouriteGifDTO(gifs.getEmailId, gifService.getActiveGifs(gifs.getGifs.keySet()))))
  }

  def unMarkAsFaviourite(id: String) = AuthenticatedAction {
    implicit request =>
      gifService.unMarkAsFaviourite(id, request.headers.get("currentUser").get)
      Ok
  }

  def getTrending() = ConsumerAction { request =>
    logger.debug("Get trending request received")
    val filter = gson.fromJson(request.body.asJson.get.toString(), classOf[Filter])
    val gifs = gifService.getTrendingGifs(filter.getIdiom, filter.getTag, filter.getTrendingPeriod, 10)
    Ok(gson.toJson(gifs))
  }

  def shareGif(id: String) = ConsumerAction {
    logger.debug(s"Share Gif ${id}")
    Ok(gson.toJson(gifService.gifShared(id)))
  }

  def getGifViewedByMe() = AuthenticatedAction { implicit request =>
    val userId = request.headers.get("currentUser").get
    logger.debug(s"Get Gif viewed by ${userId} ")
    val viewedBy = gifService.getGifViewedBy(userId)
    if(viewedBy.isEmpty)
      NotFound
    else
      Ok(gson.toJson(viewedBy))

  }

  def getGifPublishedByMeInPagesForIdiom(idiom: String, page:Int, size: Int) = AuthenticatedAction { implicit request =>
    val publishedBy = request.headers.get("currentUser").get
    logger.debug(s"Get Gif published by ${publishedBy} ")
    val gifPublishedBy = gifService.getGifPublishedByForIdiom(publishedBy, idiom, page, size)
    if(gifPublishedBy.getContent.isEmpty)
      NotFound
    else
      Ok(gson.toJson(new PageResponse[GifMetaData](gifPublishedBy.getTotalElements, gifPublishedBy.getContent)))
  }

  def getGifPublishedByMe() = AuthenticatedAction { implicit request =>
    val publishedBy = request.headers.get("currentUser").get
    logger.debug(s"Get Gif published by ${publishedBy} ")
    val gifPublishedBy = gifService.getGifPublishedBy(publishedBy)
    if(gifPublishedBy.isEmpty)
      NotFound
    else
      Ok(gson.toJson(gifPublishedBy))
  }

  def getGifPublishedByMeInPages(page:Int, size: Int) = AuthenticatedAction { implicit request =>
    val publishedBy = request.headers.get("currentUser").get
    logger.debug(s"Get Gif published by ${publishedBy} ")
    val gifPublishedBy = gifService.getGifPublishedBy(publishedBy, page, size)
    if(gifPublishedBy.getContent.isEmpty)
      NotFound
    else
      Ok(gson.toJson(new PageResponse[GifMetaData](gifPublishedBy.getTotalElements, gifPublishedBy.getContent)))
  }

  def getGifPublishedBy(publishedByBase64: String, page: Int, size: Int) = ConsumerAction { implicit request =>
    val publishedBy = new String(Base64.getDecoder().decode(publishedByBase64))
    //val publishedBy = request.headers.get("currentUser").get
    logger.debug(s"Get Gif published by ${publishedBy} ")
    val gifPublishedBy = gifService.getGifPublishedBy(publishedBy, page, size)
    if(gifPublishedBy.getContent.isEmpty)
      NotFound
    else
      Ok(gson.toJson(new PageResponse[GifMetaData](gifPublishedBy.getTotalElements, gifPublishedBy.getContent)))
  }

  def getGifTaggedBy(taggedByBase64: String, page: Int, size: Int) = ConsumerAction { implicit request =>
    val taggedBy = new String(Base64.getDecoder().decode(taggedByBase64))
    //val publishedBy = request.headers.get("currentUser").get
    logger.debug(s"Get Gif tagged by ${taggedBy} ")
    Ok(gson.toJson(gifService.getTagAuditDetails(taggedBy, page, size)))
  }



  def getContentCreators()= AdminAction{
    Ok(gson.toJson(userFinder.getContentCreatorsUserId()))
  }


  def getTaggers()= AdminAction{
    Ok(gson.toJson(userFinder.getTaggersUserId()))
  }

}


//case class Filter(idiom: String, tagName: String, duration: Int, timeUnit: TimeUnit)

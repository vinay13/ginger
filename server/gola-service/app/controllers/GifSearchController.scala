package controllers

import javax.inject.Inject

import com.google.gson.Gson
import dao.GifMetaDataDao
import dto._
import dto.DtoSerializers._
import model.{GifMetaData, Idiom, UserRole}
import org.slf4j.{Logger, LoggerFactory}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, AnyContent, Controller, Request}

import scala.concurrent.ExecutionContext.Implicits.global
import model.JsonFormats._
import net.minidev.json.JSONObject
import org.springframework.data.domain.PageRequest
import spring.service.UserService
import springconfig.context.SpringAppContext
import utils.GsonConfig

import scala.concurrent.Future
import collection.JavaConverters._

/**
  * Created by senthil
  */
class GifSearchController @Inject()() extends Controller with GingerActionBuilders {

  val gson = GsonConfig.newGson()

  override def userFinder: UserService = SpringAppContext.getUserService

  val gifService = SpringAppContext.getGifService
  val log: Logger = LoggerFactory.getLogger(this.getClass)

  def getHeaders(request: Request[AnyContent]) = {
    log.debug(s" Incoming request: ${request}")
    request.headers.toSimpleMap.foldLeft("") {
      (s: String, it: (String, String)) => s + it._1 + ":" + it._2 + " "
    }
  }

  /** *
    * called as part of CORS preflight check
    *
    * @param all
    * @return
    */
  def preflight(all: String) = Action { request =>
    log.debug(s"Headers \n ${getHeaders(request)}")
    log.debug(s"Preflight request for $all")
    Ok
  }

  def getGifForIdiom(idiom: String, page: Int, size: Int): Action[AnyContent] = ConsumerAction {
    val resultPage = gifService.findByIdiom(idiom, page, size)
    val response = new PageResponse[GifMetaData](resultPage.getTotalElements, resultPage.getContent)
    Ok(gson.toJson(response));
  }

  def getSuggestionsForIdiom(idiom: String, text: String): Action[AnyContent] = ConsumerAction {
    //suggestions.map(js => Json.fromJson[Suggestion](js))
    Ok(gson.toJson((gifService.findSuggestions(idiom, text))))
  }

  def getSuggestions(text: String): Action[AnyContent] = ConsumerAction {
    //suggestions.map(js => Json.fromJson[Suggestion](js))
    Ok(gson.toJson((gifService.findSuggestions(text))))
  }

  def getGifForIdiomAndCategory(idiom: String, category: String): Action[AnyContent] = ConsumerAction {
    val results = gifService.findByIdiomAndCategory(idiom, category)
    Ok(gson.toJson(results))
  }

  def getGifForIdiomAndText(idiom: String, text: String): Action[AnyContent] = ConsumerAction {
    val results = gifService.findByIdiomAndText(idiom, text, true).asJava
    if (results.size() > 0)
      gifService.gifSearched(idiom, text, results.get(0).getId)
    Ok(gson.toJson(results))
  }

  /*def enableGif(gifId: String, enable: Boolean, reason: String): Action[AnyContent] = AdminAction {
    val results = gifService.enableGif(gifId, enable, reason).asJava
    /*if(results.size() > 0)
      gifService.gifSearched(idiom, text, results.get(0).getId)*/
    Ok(gson.toJson(results))
  }*/

  def getRelatedGifs(idiom: String, gifId: String, pageIndex: Int, pageSize: Int): Action[AnyContent] = ConsumerAction {
    implicit request => {
      val gif = gifService.findGifById(gifId)
      if (gif == null) NotFound
      else {
        val tags = gif.getTags
        if (tags == null || tags.isEmpty) {
          log.info(s"No tags found for gif ${gifId}")
          NotFound
        }
        else {
          val tagTokens = tags.asScala.map(_.split("[ ]")).flatten

          val str = JSONObject.escape(tagTokens.mkString(" ")).replace("'", "")
          val gifs = gifService.getGifs(idiom, List(gifId), str, None, pageIndex, pageSize)
          if (gifs.getContents().isEmpty) {
            log.info(s"No related gifs found for tags ${tags}")
            NotFound
          } else Ok(gson.toJson(new PageResponse[GifMetaDataDto](gifs.getTotalCount(), gifs.getContents())))
        }
      }
    }
  }

  def getPageForPrimaryIdiomAndText(idiom: String, text: String, pageIndex: Int, pageSize: Int, exclude: Option[String]): Action[AnyContent] = ConsumerAction {
    val results = gifService.getGifs(idiom, List(), text, exclude, pageIndex, pageSize)
    if (pageIndex == 0) {
      val content = results.getContents
      if (content.size() > 0)
        gifService.gifSearched(idiom, text, content.get(0).getId)
      else
        gifService.gifSearched(idiom, text, null)
    }
    //gifService.gifSearched(idiom, text, )
    if (!results.getContents.isEmpty)
      Ok(gson.toJson(results))
    else
      NotFound
  }

  def getGifsMetaDataForPrimaryIdiomAndText(idiom: String, text: String,  pageIndex: Int, pageSize: Int): Action[AnyContent] = AdminAction {

    val results = gifService.getGifs(idiom, List(), text, None,  pageIndex, pageSize)
    if (pageIndex == 0) {
      val content = results.getContents
      if (content.size() > 0)
        gifService.gifSearched(idiom, text, content.get(0).getId)
      else
        gifService.gifSearched(idiom, text, null)
    }

    val gifIdList = results.getContents.asScala.map(_.getId).toList

    val gifMetaData = gifService.getGifByIdList(gifIdList.asJava)

    val response = new PageResponse[GifMetaData](results.getTotalCount,gifMetaData)
    if (!results.getContents.isEmpty)
      Ok(gson.toJson(response))
    else
      NotFound
  }





  def getPageForIdiomAndText(idiom: String, text: String, pageIndex: Int, pageSize: Int): Action[AnyContent] = ConsumerAction {
    val results = gifService.findByIdiomAndText(idiom, text, true, pageIndex, pageSize)
    if (pageIndex == 0) {
      val content = results.getContent
      if (content.size() > 0)
        gifService.gifSearched(idiom, text, content.get(0).getId)
      else
        gifService.gifSearched(idiom, text, null)
    }
    if (!results.getContent.isEmpty)
      Ok(gson.toJson(new PageResponse[GifMetaData](results.getTotalElements, results.getContent)))
    else
      NotFound
  }

  def getMyGifsForIdiomAndText(idiom: String, text: String, pageIndex: Int, pageSize: Int): Action[AnyContent] = AuthenticatedAction { request => {
    val publishedBy = request.headers.get("currentUser").get
    val results = gifService.findByIdiomAndText(idiom, publishedBy, text, pageIndex, pageSize)
    if (!results.getContent.isEmpty)
      Ok(gson.toJson(new PageResponse[GifMetaData](results.getTotalElements, results.getContent)))
    else
      NotFound
  }
  }


  def cleanUpTags(): Action[AnyContent] = AdminAction { implicit request => {
      gifService.removeTagsAboveWords(5)
    Ok
  }
  }

  def getGifsWithNoTags(idiom: String, page: Int, size: Int): Action[AnyContent] = AuthAction(UserRole.Admin :: UserRole.Tagger :: Nil) {
    val tags = gifService.getGifWithNoTags(Idiom.valueOf(idiom), page, size)
    Ok(gson.toJson(new PageResponse(tags.getTotalElements, tags.getContent)))
  }

  def getMyGifsWithNoTags(idiom: String, page: Int, size: Int): Action[AnyContent] = AuthenticatedAction { request => {
    val publishedBy = request.headers.get("currentUser").get
    val tags = gifService.getGifWithNoTags(publishedBy, Idiom.valueOf(idiom), page, size)
    Ok(gson.toJson(new PageResponse(tags.getTotalElements, tags.getContent)))
  }
  }

  def getTopSearches(idiom: String, trendingPeriod: String): Action[AnyContent] = ConsumerAction {
    Ok(gson.toJson(gifService.getTopSearches(idiom, TrendingPeriod.valueOf(trendingPeriod),0, 100).asJava))
  }

  def getGifViewed(idiom: String, trendingPeriod: String): Action[AnyContent] = ConsumerAction {
    Ok(gson.toJson(gifService.getTopSearches(idiom, TrendingPeriod.valueOf(trendingPeriod), 0, 100).asJava))
  }

  def filterGifs(page: Int, size: Int): Action[AnyContent] = ConsumerAction {
    implicit request => {
      val searchFilter = gson.fromJson(request.body.asJson.get.toString(), classOf[SearchFilter])
      val pageResult = gifService.searchGif(searchFilter, page, size)
      Ok(gson.toJson(new PageResponse[GifMetaData](pageResult.getTotalElements, pageResult.getContent)))
    }
  }
  def getTbdTags()=AdminAction{ implicit request =>
    val text="tbd"

   Ok(gson.toJson(gifService.findTbdTags(text)))

  }


}

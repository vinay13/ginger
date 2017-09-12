package controllers

import java.io.File
import java.util.TimeZone
import javax.inject._

import com.google.gson.Gson
import dto._
import model.{GifMetaData, Tab}
import org.slf4j.LoggerFactory.getLogger
import play.api._
import play.api.mvc._
import spring.service.{GifService, TabService, UserService}
import springconfig.context.SpringAppContext
import utils.GsonConfig

import collection.JavaConverters._

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject() extends Controller with GingerActionBuilders {

  val tabService: TabService = SpringAppContext.getTabService

  val gifService: GifService = SpringAppContext.getGifService

  override  val userFinder: UserService = SpringAppContext.getUserService


  private val logger = getLogger(classOf[HomeController])

  val gson = GsonConfig.newGson()

  def getTabs(idiom: String) = ConsumerAction {
    Ok(gson.toJson(tabService.getTabs(idiom)))
  }

  def getOrderedTopItems(idiom: String, tabId: String) = AdminAction { request =>
    val tab = tabService.getTab(tabId)
    if (tab.isCurated) {
      Ok(gson.toJson(tabService.getOrderedTopItems(tabId)))
    }else
      Ok
  }

  def getAllTopItems(idiom: String, tabId: String):Action[AnyContent] =
    getTopItems(idiom, tabId, 0, 1000)


  def getTopItemPages(tabId: String, from: Int, size: Int) = ConsumerAction { request =>
    val tab = tabService.getTab(tabId)
    if (tab.isCurated && tab.isTrending) {
      val pages = tabService.getPages(tabId, from, size)
      Ok(gson.toJson(new PageResponse(pages.getTotalElements, pages.getContent)))
    }else{
      NotFound
    }
  }


  def getTopItems(idiom: String, tabId: String, from: Int, size: Int) = ConsumerAction { request =>
    val tab = tabService.getTab(tabId)
    if (tab.isCurated) {
      Ok(gson.toJson(tabService.getCuratedTabData(tabId, TimeZone.getTimeZone("GMT+530"), from, size)))
    } else {
      if (tab.isSearch) {
        Ok(gson.toJson(gifService.getTopSearches(idiom, tab.getTrendingPeriod, from, size).asJava))
      } else if(tab.isTrending) {
        Ok(gson.toJson(gifService.getTopItems(idiom, tab.getTrendingPeriod, from, size).asJava))
      }else{
        val results: java.util.List[TopItem] = tab.getTags.asScala.map(tag => {
          logger.info(s"Querying top shares for ${tag}")
          gifService.getTopShare(idiom, tag, tab.getTrendingPeriod) match {
            case Some(result) => {
              result.setText(tag)
              Some(result)
            }
            case None => None
          }
        }).flatten.toList.asJava
        Ok(gson.toJson(results))
      }
    }
  }


  def updateAllTopItems()=AdminAction {
    request =>
      tabService.updateTopItems()
      Ok

  }

  def addItemToTrendingTab(idiom:String)=AdminAction {
    request =>

      val json = request.body.asJson.get.toString()
      val item = gson.fromJson(json, classOf[TopItem])
      tabService.addTopItemToTrendingTab(idiom,item)
      Ok

  }

  def removeItemFromTrendingTab(idiom:String,gifId:String)=AdminAction{
    request =>
      tabService.removeItemFromTrendingTabByGifId(idiom,gifId)
      Ok
  }

  def getAllTrendingTabItems(idiom:String)=AdminAction {
    request =>
      val items = tabService.getAllTrendingTabItems(idiom)
      if(items!=null)
      Ok(gson.toJson(items))
      else NotFound

  }


}

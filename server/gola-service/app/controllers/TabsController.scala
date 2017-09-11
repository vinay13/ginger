package controllers

import java.util
import javax.inject.Inject

import com.google.gson.Gson
import dto.{TopItem, TopItemData, TrendingPeriod}
import model.{CuratedTab, Idiom, Tab, Tabs}
import play.api.mvc.{Action, Controller}
import spring.service.{TabService, UserService}
import springconfig.context.SpringAppContext
import utils.GsonConfig

import scala.collection.mutable

/**
  * Created by senthil
  */
class TabsController  @Inject() extends Controller  with GingerActionBuilders {

  val tabService:TabService = SpringAppContext.getTabService

  override def userFinder: UserService = SpringAppContext.getUserService

  val gson = GsonConfig.newGson()

  def createDefaultTabs()  = AdminAction {
    createDefaultTamilTab
    createDefaultHindiTab
    Ok
  }

  def createTabs() = AdminAction { request =>
    val tabs = gson.fromJson(request.body.asJson.get.toString(), classOf[Tabs])
    val savedTabs:Tabs = tabService.saveTabs(tabs,List())
    Ok(gson.toJson(savedTabs))
  }

  def saveTab(idiom: String) = AdminAction { request =>
    val tab = gson.fromJson(request.body.asJson.get.toString(), classOf[Tab])
    tabService.saveTabByIdiom(tab,Idiom.valueOf(idiom))
    Ok
  }
  def removeTab(idiom: String,tabId:String) = AdminAction { request =>
    val tabs = tabService.removeTabByTabId(Idiom.valueOf(idiom),tabId)
    Ok(gson.toJson(tabs))
  }

  private def createDefaultTamilTab = {
    val tabs = new Tabs()
    tabs.setIdiom(Idiom.Tamil)

    val trendingTamilTab = new Tab("Trending", new util.LinkedList[String](), false, true, 1, false, TrendingPeriod.Daily)
    trendingTamilTab.setId("1")

    val celebrityTamilTab = new Tab("Celebrity", util.Arrays.asList("Rajini", "Vadivel", "Surya", "Ajith", "Movie", "MovieStar"), false, false, 2, false, TrendingPeriod.Daily)
    celebrityTamilTab.setId("2")

    val emotionsTamilTab = new Tab("Emotions", util.Arrays.asList("Love", "Anger", "Content", "Universal"), false, false, 3, false, TrendingPeriod.Daily)
    emotionsTamilTab.setId("3")

    val searchTamilTab = new Tab("Search", util.Arrays.asList("Dhoni", "Katrina", "Anger"), false, false, 3, true, TrendingPeriod.Daily)
    searchTamilTab.setId("4")

    tabs.getTabs.add(trendingTamilTab)
    tabs.getTabs.add(celebrityTamilTab)
    tabs.getTabs.add(emotionsTamilTab)
    tabs.getTabs.add(searchTamilTab)
    //tabService.saveTabs(tabs)
    tabs
  }

  private def createDefaultHindiTab = {
    val tabs = new Tabs()
    tabs.setIdiom(Idiom.Hindi)

    val trendingTab = new Tab("Trending", new util.LinkedList[String](), false, true, 1, false, TrendingPeriod.Daily)
    trendingTab.setId("5")

    val celebrityTab = new Tab("Celebrity", util.Arrays.asList("Amithabh", "Amir Khan", "Deepika", "Ajay Devgan", "Movie", "MovieStar"), false, false, 2, false, TrendingPeriod.Daily)
    celebrityTab.setId("6")

    val emotionsTab = new Tab("Emotions", util.Arrays.asList("Love", "Anger", "Content", "Universal"), false, false, 3, false, TrendingPeriod.Daily)
    emotionsTab.setId("7")

    val searchTab = new Tab("Search", util.Arrays.asList("Dhoni", "Katrina", "Anger"), false, false, 3, true, TrendingPeriod.Daily)
    searchTab.setId("8")

    tabs.getTabs.add(trendingTab)
    tabs.getTabs.add(celebrityTab)
    tabs.getTabs.add(emotionsTab)
    tabs.getTabs.add(searchTab)
    //tabService.saveTabs(tabs)
    tabs
  }

  def saveCuratedTab() = AdminAction { request =>
    val json = request.body.asJson.get.toString()
    val curatedTab = gson.fromJson(json, classOf[CuratedTab])
    tabService.saveCuratedTab(curatedTab)
    Ok
  }


}

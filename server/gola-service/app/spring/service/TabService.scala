package spring.service

import java.util
import java.util.{Base64, Calendar, Collections, TimeZone}

import com.google.gson.Gson
import com.mongodb.{DBCollection, DBObject}
import com.mongodb.util.JSON
import dto.{GifMetaDataDto, TagUploadDto,TopItem, TopItemDto}
import model.{CuratedTab, GifMetaData, Idiom, Tabs}
import model._
import org.bson.types.ObjectId
import org.quartz.CronExpression
import org.slf4j.LoggerFactory.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.domain.{PageRequest, Pageable, Sort}
import org.springframework.data.mongodb.core.query.{Criteria, Query, Update}
import org.springframework.data.mongodb.core.{CollectionCallback, MongoTemplate}
import org.springframework.stereotype.Service
import springconfig.repo.{CuratedTabRepository, GifMetaDataRepository, GifShareRepository, TabsRepository}
import utils.GifMetaDataConverter

import collection.JavaConverters._
import scala.collection.mutable

/**
  * Created by senthil
  */

@Service
class TabService(@Autowired mongoTemplate: MongoTemplate,@Autowired gifService: GifService,
                 @Autowired tabsRepository: TabsRepository, @Autowired curatedTabRepository: CuratedTabRepository,
                 @Autowired gifRepository: GifMetaDataRepository,
                 @Autowired gifShareRepository: GifShareRepository) {


  private val logger = getLogger(classOf[TabService])

  def getTabs(idiom: String) = tabsRepository.findByIdiom(Idiom.valueOf(idiom))


  def removeTabByTabId(idiom: Idiom, tabId: String) = {
    val tabs = tabsRepository.findByIdiom(idiom)
    tabs.removeTabForTabId(tabId)
    tabsRepository.save(tabs)
  }




  def saveTabByIdiom(tab: Tab, idiom: Idiom) = {
    val tabs = tabsRepository.findByIdiom(idiom)
    var tabsList=new util.LinkedList(tabs.getTabs)
    if(!tabs.hasTabForTabId(tab.getId))
      tabsList.add(tab)

    val tabItems = tabsList.asScala.map(tabItem => {
      if (tabItem.getId.equals(tab.getId)) tab
      else tabItem
    }).toList
    tabs.setTabs(tabItems.asJava)
    tabsRepository.save(tabs)
  }

  def createTopItem(tag: String, idiom: Idiom, uploadDtos: List[TagUploadDto]) = {
    val gifData = gifService.getGifs(String.valueOf(idiom), List(), tag, None,0, 1)
    if (!gifData.getContents.isEmpty) {
      val gif = gifData.getContents.get(0)
      val item = new TopItem()
      item.setActive(true)
      item.setText(tag)
      setDisplayNameAndExcludeToTopItem(uploadDtos, item)
      /*val displayName = textDisplayNameMap.get(tag)
      displayName.map(name=>item.setDisplayName(displayName.get))*/
      item.setGifId(gif.getId)
      item.setBaseUrl(gif.getBaseUrl)
      item.setOriginalFN(gif.getOriginalFN)
      item.setLowResFN(gif.getLowResFN)
      item.setThumbNailFN(gif.getThumbNailFN)
      item.setLowResWebpFN(gif.getLowResWebpFN)
      item;
    }
    else new TopItem(tag)
  }

  def saveTab(tab: Tab, idiom: Idiom, uploadDtos: List[TagUploadDto]) = {
    var curatedTab = curatedTabRepository.findOne(tab.getId)
    if (curatedTab == null) {
      curatedTab = new CuratedTab()
      curatedTab.setId(tab.getId)
    }
    logger.info(s"curated tab name is " + tab.getName)
    logger.info(s"Curated tabs topitems ${curatedTab.getTopItems.asScala.map(_.getText).toList}")
    val tags = tab.getTags.asScala
    tags.foreach(tag => {
      if (!curatedTab.containsTopItemForText(tag)) {
        val item = createTopItem(tag, idiom, uploadDtos)
        curatedTab.getTopItems.add(item);
      }
      /*else {
         val topItems=curatedTab.getTopItems.asScala.map(topItem=> {
           if(topItem.getBaseUrl==null)
             createTopItem(tag)
           else
             topItem
         }).toList
         curatedTab.setTopItems(topItems.asJava)
        }*/

    })
    curatedTab.getTopItems.asScala.map(item => {
      setDisplayNameAndExcludeToTopItem(uploadDtos, item)
    })


    val topItems = curatedTab.getTopItems.asScala.map(_.getText)
    logger.info(s"Top items ${topItems.toList}")
    val staleItems = topItems.filterNot(tab.getTags.contains(_))
    logger.info(s"stale items items ${staleItems.toList}")
    staleItems.foreach(curatedTab.removeTopitemForText(_))
    logger.info(new Gson().toJson(curatedTab))
    curatedTabRepository.save(curatedTab)

    saveTabByIdiom(tab, idiom)
  }


  private def setDisplayNameAndExcludeToTopItem(uploadDtos: List[TagUploadDto], item: TopItem) = {
    val dtos = uploadDtos.find(data => data.getText.equals(item.getText))
    dtos.map(data => {
      item.setDisplayName(data.getDisplayName)
      item.setExclude(data.getExclude)
      item.setOrder(data.getOrder)
      logger.info(s"setting the order of tabs ${data.getOrder} ${item.getText}")
    })
    /*if (!dtos.isEmpty) {
      val uploadData = dtos.asJava.get(0)

    }*/
  }



  def saveTabs(tabs: Tabs,uploadDtos:List[TagUploadDto]) = {

    tabs.getTabs.asScala.filter(!_.isTrending).foreach(tab => {

      saveTab(tab, tabs.getIdiom, uploadDtos)


      /*val usedTopItems = curatedTab.getTopItems.asScala.filter(topItem => tab.getTags.contains(topItem.getText))
    val usedTags = usedTopItems.map(_.getText)
    val tagsCopy = new util.LinkedList[String](tab.getTags)
    tagsCopy.removeAll(usedTags.asJava)
    val newItems = tagsCopy.asScala.map(tag => {
      val topItem = new TopItem()
      topItem.setText(tag)
      topItem
    })
    curatedTab.setTopItems((usedTopItems ++ newItems).toList.asJava)
    curatedTabRepository.save(curatedTab)*/


    })
    tabsRepository.save(tabs)
  }

  def getTab(id: String) = tabsRepository.findByTabsId(id).getTabs.asScala.find(_.getId.equals(id)).get

  def getPages(tabId: String, page: Int, size: Int) = {
    val curatedTab = curatedTabRepository.findOne(tabId)
    val tab = tabsRepository.findByTabsId(tabId).getTabs.asScala.find(_.getId.equals(tabId)).get
    if (curatedTab != null && tab.isCurated) {

      val curatedGifIds = curatedTab.getTopItems.asScala.map(_.getGifId)
      gifRepository.findActiveGifNotIn(curatedGifIds.asJava, new PageRequest(page, size, new Sort(Direction.DESC, "shareCount")))
    } else {
      val curatedGifIds = gifShareRepository.findAll().asScala.map(_.getGifId)
      gifRepository.findActiveGifNotIn(curatedGifIds.asJava, new PageRequest(page, size, new Sort(Direction.DESC, "shareCount")))
    }
  }

  def getOrderedTopItems(id: String): util.List[TopItem] = {
    val curatedTab = curatedTabRepository.findOne(id)

    if (curatedTab != null) {

      curatedTab.getTopItems.asScala.filter(_.isActive).asJava
    } else
      Collections.emptyList()
  }

  def getCuratedTabData(id: String, timeZone: TimeZone, page: Int, size: Int):util.List[TopItemDto] = {
    val  curatedTab = mongoTemplate.execute("curatedTab", new CollectionCallback[CuratedTab] {
      override def doInCollection(collection: DBCollection): CuratedTab = {
        logger.info("$slicing pages " + (page * size) + ", " + size)
        val projection = JSON.parse("{_id: 1, topItems: {$slice: [" + (page * size) + ", " + size + "]}}").asInstanceOf[DBObject];
        val query = JSON.parse("{\"_id\":\"" + id + "\",  \"topItems.active\": true}} ").asInstanceOf[DBObject];
        val results = collection.find(query, projection)
        if (results.size() > 0)
          results.iterator().asScala.map(r => mongoTemplate.getConverter.read(classOf[CuratedTab], r)).toList.head
        else
          null
      }
    })
    //val curatedTab = curatedTabRepository.findByTabsId(id, page * size, size)



    if (curatedTab != null) {

      val activeTopItems = curatedTab.getTopItems.asScala
      logger.info(s"active top items are ${activeTopItems}")
      val timeSpecificGifs = activeTopItems.filter(_.getTriggerExpr != null)
      logger.info(s"Time specific gifs are ${timeSpecificGifs}")
      val restOfTheGifs = activeTopItems.filter(_.getTriggerExpr == null).sortBy(item => item.getOrder())
      val triggeringGifs = timeSpecificGifs.filter(topItem => {
        val cronExprStr: String = topItem.getTriggerExpr()
        try {
          val cronExpr = new CronExpression(cronExprStr)
          cronExpr.setTimeZone(timeZone)
          val timeZoneDate = Calendar.getInstance(timeZone).getTime()
          val date = cronExpr.getTimeAfter(timeZoneDate)
          logger.info(s"cronExpression triggered ?  ${(System.currentTimeMillis() + 120 * 1000 >= date.getTime)}")
          System.currentTimeMillis() + 120 * 1000 >= date.getTime
        } catch {
          case e: Exception => {
            logger.info(s"Exception parsing cron expression ${cronExprStr}")
            true
          }
        }
      })
      (triggeringGifs ++ restOfTheGifs).map(GifMetaDataConverter.convertTopItemToDto(_)).asJava
    }
    else
      new util.LinkedList()
  }

  def saveCuratedTab(tab: CuratedTab) = curatedTabRepository.save(tab)


  def uploadTags(idiom: Idiom, tabId: String, tagArrayList: List[String]):Tabs = {
    val tabs = tabsRepository.findByIdiom(idiom)

    if (tabs != null) {
      val tabList = tabs.getTabs
      tabList.asScala.foreach(tab => {
        if (tab.getId.equals(tabId)) {
          tab.setTags(tagArrayList.asJava)
        }
      })

    }

    tabs
  }

  def updateTopItems() = {
    val curatedTabs = curatedTabRepository.findAll()
    curatedTabs.asScala.map(curatedTab => {
      curatedTab.getTopItems.asScala.map(item => {
        if (item.getGifId != null)
          updateCuratedTabTopItems(item.getGifId)

      })
    })
  }

  def updateCuratedTabTopItems(gifId: String) = {
    val gifMetaData = gifService.getGifById(gifId)
    if (gifMetaData != null) {
      val orignalFN = if (gifMetaData.isWaterMarked) gifMetaData.getWaterMarkedFN else gifMetaData.getOriginalFN
      mongoTemplate.updateFirst(new Query(Criteria.where("topItems.gifId").is(gifMetaData.getId)), new Update()
        .set("topItems.$.baseUrl", gifMetaData.getBaseUrl)
        .set("topItems.$.width", gifMetaData.getWidth)
        .set("topItems.$.height", gifMetaData.getHeight)
        .set("topItems.$.originalFN", orignalFN)
        .set("topItems.$.thumbNailFN", gifMetaData.getThumbNailFN)
        .set("topItems.$.lowResWidth", gifMetaData.getLowResWidth)
        .set("topItems.$.lowResHeight", gifMetaData.getLowResHeight)
        .set("topItems.$.lowResFN", gifMetaData.getLowResFN)
        .set("topItems.$.lowResWebpFN", gifMetaData.getLowResWebpFN)
        , classOf[CuratedTab])
    }
  }


  def addTopItemToTrendingTab(idiom: String, item: TopItem) = {
    val tab = getTrendingTabByIdiom(idiom)
    if(tab!=null) {
      var curatedTab = curatedTabRepository.findOne(tab.getId)
      if (curatedTab == null) {
        curatedTab = new CuratedTab();
        curatedTab.setId(tab.getId)
      }
      if (!curatedTab.containsTopItemForGifId(item.getGifId)) {
        curatedTab.getTopItems.add(0,item);
        curatedTabRepository.save(curatedTab)
      }
    }

  }

  private def getTrendingTabByIdiom(idiom: String): Tab = {
    val tabs = tabsRepository.findByIdiom(Idiom.valueOf(idiom))
    val trendingTabs = tabs.getTabs.asScala.filter(tab => {
      tab.isTrending
    }).toList
    if (!trendingTabs.isEmpty) {
      val tab = trendingTabs.asJava.get(0)
      tab
    }
    else
      null
  }

  def getAllTrendingTabItems(idiom: String) = {
    val tab = getTrendingTabByIdiom(idiom);
    if(tab!=null) {
      var curatedTab = curatedTabRepository.findOne(tab.getId)
      if (curatedTab != null)
        curatedTab.getTopItems
      else
        new util.LinkedList[TopItem]()
    }
    else
      null
  }

  def removeItemFromTrendingTabByGifId(idiom: String, gifId: String) = {
    val tab = getTrendingTabByIdiom(idiom);
    if(tab!=null) {
      var curatedTab = curatedTabRepository.findOne(tab.getId)
      if (curatedTab != null) {
        curatedTab.removeTopitemForGifId(gifId)
        curatedTabRepository.save(curatedTab)
      }
    }
  }


}

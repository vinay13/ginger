package spring.service

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import java.security.MessageDigest
import java.util
import java.util.regex.Pattern
import java.util.stream.Collectors
import java.util.{Base64, Calendar, Date, TimeZone}

import actors.{GenerateLowResGifFromBytes, LowResGifActor}
import akka.actor.ActorRef
import com.google.gson.Gson
import utils.{GifMetaDataConverter, GsonConfig, S3Uploader}
import com.mongodb.{BasicDBObject, DBCollection, DBObject, ReadPreference}
import dto._
import exception.GifAlreadyExistsException
import model._
import org.bson.types.ObjectId
import org.quartz.CronExpression
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.{Page, PageImpl, PageRequest, Sort}
import org.springframework.data.mongodb.core.{CollectionCallback, MongoTemplate}
import org.springframework.data.mongodb.core.aggregation.Aggregation._
import org.springframework.data.mongodb.core.aggregation._
import org.springframework.data.mongodb.core.query.{Criteria, Query, TextCriteria, Update}
import org.springframework.stereotype.Service
import springconfig.repo._
import org.slf4j.LoggerFactory.getLogger
import play.api.mvc.Result
import play.api.mvc._
import reactivemongo.bson.BSONObjectID
import com.mongodb.util.JSON

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


/**
 * Created by senthil
 */

@Service
class GifService(@Autowired gifRepository: GifMetaDataRepository,
                 @Autowired gifHitRepository: GifHitRepository,
                 @Autowired trendingGifRepository: TrendingGifRepository,
                 @Autowired gifShareRepository: GifShareRepository,
                 @Autowired searchHitRepository: SearchHitRepository,
                 @Autowired gifSensorAuditRepository: GifSensorAuditRepository,
                 @Autowired curatedTabRepository: CuratedTabRepository,
                 @Autowired tagsRepository: AllTagsRepository,
                 @Autowired gifConversionRepository: GifConversionRepository,
                 @Autowired faviourteGifRepository: FaviourteGifRepository,
                 @Autowired categoryTagsRepository: CategoryTagsRepository,
                 @Autowired lowResGifGenerationRepository: LowResGifGenerationRepository,
                 @Autowired tagUpdateAuditRepository: TagUpdateAuditRepository,
                 @Autowired mongoTemplate: MongoTemplate) {


  def migrateBaseUrl() = {
    /*gifRepository.findAll().asScala.foreach(gif => {
      gif.setBaseUrl(gif.getBaseUrl.replace("gola-gif-dev-store-cf.xpresso.me", "gola-gif-prod-cf.xpresso.me"))
      logger.info(s"base url migrated for ${gif.getId} to ${gif.getBaseUrl}")
      gifRepository.save(gif)
    })*/

    curatedTabRepository.findAll().asScala.foreach(tab => {
      tab.getTopItems.asScala.foreach(item => {
        logger.info(s"base url migrated for ${item.getGifId} to ${item.getBaseUrl}")
        if(item.getBaseUrl != null) {
          item.setBaseUrl(item.getBaseUrl.replace("gola-gif-dev-store-cf.xpresso.me", "gola-gif-prod-cf.xpresso.me"))
        }
        logger.info(s"base url migrated for ${item.getGifId} to ${item.getBaseUrl}")
      })
      curatedTabRepository.save(tab)
    })
  }


  def removeTagsAboveWords(numberOfWorlds: Int) = {
    gifRepository.findAll().asScala.foreach(gif => {
      val tags = gif.getTags.asScala.filter(_.split("[ ]").length <= numberOfWorlds)
      gif.setTags(tags.asJava)
      gifRepository.save(gif)
    })
  }


  private val logger = getLogger(classOf[GifService])

  val gson = GsonConfig.newGson()

  def findGifById(id: String) = {
    gifRepository.findOne(id)
  }

  def findGifWithoutLowRes() = {
    gifRepository.findBylowResFNExists(false)
  }

  def findGifWithoutWaterMark() = {
    gifRepository.findNonWaterMarkedGifs()
  }

  def findGifWithLowRes() = {
    gifRepository.findBylowResFNExists(true)
  }

  def findGifUsingFileName(fileName: String) = {
    val pattern = Pattern.compile(Pattern.quote(fileName))
    gifRepository.findGifUsingFileName(pattern)
  }


  def findByIdiomAndText(idiom: String, text: String, active: Boolean) = {
    val pattern = Pattern.compile(Pattern.quote(text), Pattern.CASE_INSENSITIVE)
    gifRepository.findByIdiomAndText(idiom, pattern, active, new PageRequest(0, 10, sortByPublishedOn())).getContent.asScala
  }

  def enableGif(gifId: String, enable: Boolean, reason: String, by: String) = {
    mongoTemplate.findAndModify(new Query(Criteria.where("id").is(new ObjectId(gifId))), new Update().set("active", enable), classOf[GifMetaData])
    mongoTemplate.updateMulti(new Query(Criteria.where("gifId").is(new ObjectId(gifId))), new Update().set("active", enable), classOf[GifHit])
    mongoTemplate.updateMulti(new Query(Criteria.where("gifId").is(new ObjectId(gifId))), new Update().set("active", enable), classOf[GifShare])
    mongoTemplate.updateMulti(new Query(Criteria.where("gifId").is(new ObjectId(gifId))), new Update().set("active", enable), classOf[SearchHit])
    val curatedTabs = curatedTabRepository.findByTopItemsGifId(gifId)
    curatedTabs.asScala.foreach(tab => {
      for (elem <- tab.getTopItems.asScala.filter(topItem => topItem.getGifId.equals(gifId))) {
        elem.setActive(enable);
      }
    })
    curatedTabRepository.save(curatedTabs)
    gifSensorAuditRepository.save(new GifCensorAudit(gifId, reason, by, enable))
  }

  def findByIdiomAndTags(idiom: String, text: Seq[String], active: Boolean, pageIndex: Int, pageSize: Int) = {
    import collection.JavaConverters._
    val patterns = text.map(t => Pattern.compile(Pattern.quote(t), Pattern.CASE_INSENSITIVE)).toList.asJava
    val query = new Query(Criteria.where("idiom").is(idiom).and("tags").in(patterns).and("active").is(active)).`with`(new PageRequest(pageIndex, pageSize))
    gifRepository.findByIdiomAndTags(idiom, patterns, active, new PageRequest(pageIndex, pageSize, sortByPublishedOn()))
    //mongoTemplate.find(query, classOf[GifMetaData])
  }

  def findByIdiomAndText(idiom: String, text: String, active: Boolean, pageIndex: Int, pageSize: Int) = {
    val pattern = Pattern.compile(Pattern.quote(text), Pattern.CASE_INSENSITIVE)
    gifRepository.findByIdiomAndText(idiom, pattern, active, new PageRequest(pageIndex, pageSize, sortByPublishedOn()))
  }

  def findByIdiomAndText(idiom: String, publishedBy: String, text: String, pageIndex: Int, pageSize: Int) = {
    val pattern = Pattern.compile(Pattern.quote(text), Pattern.CASE_INSENSITIVE)
    gifRepository.findByIdiomAndText(idiom, publishedBy, pattern, new PageRequest(pageIndex, pageSize, sortByPublishedOn()))
  }

  def findByIdiomAndCategory(idiom: String, text: String) = {
    val pattern = Pattern.compile(Pattern.quote(text), Pattern.CASE_INSENSITIVE)
    //gifRepository.findByIdiomsAndCategories(idiom, pattern)
  }

  def findBySha256(sha256: String) = {
    gifRepository.findBySha256(sha256)
  }

  def findByIdiom(idiom: String, page: Int, size: Int) = {
    gifRepository.findByIdioms(Idiom.valueOf(idiom), new PageRequest(page, size, sortByPublishedOn()))
  }

  def findSuggestions(idiom: String, text: String): java.util.List[String] = {
    val results = gifRepository.findSuggestions(idiom, Pattern.compile(Pattern.quote(text), Pattern.CASE_INSENSITIVE), true).asScala
    val sortedTags = results.map(s => s.getTags.asScala).flatten.toSet.toList.sorted
    val lowerCaseText = text.toLowerCase

    (sortedTags.filter(tag => {
      val lowerCaseTag = tag.toLowerCase
      lowerCaseTag.startsWith(lowerCaseText)
    }) ++ sortedTags.filter(_.toLowerCase.contains(lowerCaseText))).take(5).asJava
  }

  def findSuggestions(text: String): java.util.List[String] = {
    val results = gifRepository.findSuggestions(Pattern.compile(Pattern.quote(text), Pattern.CASE_INSENSITIVE), true).asScala
    results.map(s => s.getTags.asScala).flatten.map(_.toLowerCase.trim()).toSet.toList.asJava
  }

  def updateTags(id: String, tags: List[String], manualTagUpdate: Boolean, currentTags: util.List[String], user: User) = {
    checkTagModificationByTagger(id, tags, currentTags, user)
   /* var searchTags =new util.LinkedList(tags.asJava)
    val tagsWithoutSpace = tags.map(tag => {
      tag.replaceAll(" ", "")
    }).toList
    searchTags.addAll(tagsWithoutSpace.asJava)*/
    mongoTemplate.findAndModify(Query.query(Criteria.where("id").is(new ObjectId(id))), new Update().set("tags", tags.asJava), classOf[GifMetaData])
    mongoTemplate.findAndModify(Query.query(Criteria.where("id").is(new ObjectId(id))), new Update().set("tagCorrected", manualTagUpdate), classOf[GifMetaData])
    updateAllTags(tags)
  }

  def getTagsUpdateCountBy(userId: String) = {
    tagUpdateAuditRepository.findByTaggerId(userId).asScala.map(_.getGifId).toSet.size
  }

  def checkTagModificationByTagger(gifId: String, tags: List[String], currentTags: util.List[String], user: User) = {
    if (user.getUserRoles.contains(UserRole.Tagger)) {
      val newTags = new util.LinkedList(tags.asJava)
      logger.info(s"New Tags is ${newTags}")
      logger.info(s"current Tags is ${currentTags}")
      newTags.removeAll(currentTags)
      if (newTags.size() > 0) {
        "Added"
      }
      currentTags.removeAll(tags.asJava)
      if (currentTags.size() > 0) {
        "Removed"
      }

      val tagUpdateAudit = new TagUpdateAudit
      tagUpdateAudit.setGifId(gifId)
      tagUpdateAudit.setTaggerId(user.getEmailId)
      tagUpdateAudit.setTagsAdded(newTags)
      tagUpdateAudit.setTagsRemoved(currentTags)
      tagUpdateAudit.setTimestamp(new Date())
      tagUpdateAuditRepository.save(tagUpdateAudit)

    }
  }

  private def updateAllTags(tags: List[String]) = {
    Future {
      val allTags = tagsRepository.findAll();
      if (!allTags.isEmpty) {
        allTags.get(0).getTags.addAll(tags.asJava)
        tagsRepository.save(allTags)
      } else {
        val newTags = new AllTags();
        newTags.setTags(tags.toSet.asJava)
        tagsRepository.save(newTags)
      }
    }
  }

  def updateCategories(id: String, categories: List[String]) = {
    mongoTemplate.findAndModify(Query.query(Criteria.where("id").is(new ObjectId(id))), new Update().set("categories", categories.asJava), classOf[GifMetaData])
  }

  def updateIdioms(id: String, idioms: java.util.List[String]) = {
    mongoTemplate.findAndModify(Query.query(Criteria.where("id").is(new ObjectId(id))), new Update().set("idioms", idioms), classOf[GifMetaData])
  }

  def updateSource(id: String, source: String) = {
    mongoTemplate.findAndModify(Query.query(Criteria.where("id").is(new ObjectId(id))), new Update().set("source", source), classOf[GifMetaData])
  }

  def updateTriggerData(id: String, triggerExpr: String) = {
    mongoTemplate.findAndModify(Query.query(Criteria.where("id").is(new ObjectId(id))), new Update().set("triggerExpr", triggerExpr), classOf[GifMetaData])

    mongoTemplate.updateMulti(Query.query(Criteria.where("topItems.gifId").is(id)), new Update().set("topItems.$.triggerExpr", triggerExpr), classOf[CuratedTab])

    /*val curatedTabs = curatedTabRepository.findByTopItemsGifId(id)
    curatedTabs.asScala.foreach(tab => {
      for (elem <- tab.getTopItems.asScala.filter(topItem => topItem.getGifId.equals(gifId))) {
        elem.setActive(enable);
      }
    })
    curatedTabRepository.save(curatedTabs)*/
  }

  def deleteTriggerData(id: String) = {
    mongoTemplate.findAndModify(Query.query(Criteria.where("id").is(new ObjectId(id))), new Update().unset("triggerExpr"), classOf[GifMetaData])

    mongoTemplate.updateMulti(Query.query(Criteria.where("topItems.gifId").is(id)), new Update().unset("topItems.$.triggerExpr"), classOf[CuratedTab])
  }

  def saveGifMetaData(gifMetaData: GifMetaData) = {
    val savedData = gifRepository.save(gifMetaData)
    updateAllTags(gifMetaData.getTags.asScala.toList)
    savedData
  }

  def gifShared(id: String): Unit = {
    val gifMetaData = gifRepository.findOne(id)
    val gifShare = new GifShare()
    gifShare.setGifId(id)
    gifShare.setIdioms(gifMetaData.getIdioms)
    gifShare.setTimestamp(new Date())
    gifShare.setTags(gifMetaData.getTags)
    gifShareRepository.save(gifShare)
    mongoTemplate.findAndModify(Query.query(Criteria.where("id").is(new ObjectId(id))), new Update().inc("shareCount", 1), classOf[GifMetaData])
  }

  def markAsFaviourite(gifId: String, userId: String) = {
    var favouriteGif = faviourteGifRepository.findByEmailId(userId)
    if (favouriteGif == null) {
      favouriteGif = new FavouriteGif(userId)
    }

    favouriteGif.getGifs.put(gifId, new Date())
    faviourteGifRepository.save(favouriteGif)
    mongoTemplate.findAndModify(Query.query(Criteria.where("id").is(new ObjectId(gifId))), new Update().inc("favCount", 1), classOf[GifMetaData])
  }

  def getFaviourites(userId: String) = {
    var favouriteGif = faviourteGifRepository.findByEmailId(userId)
    if (favouriteGif == null) {
      new FavouriteGif();
    } else
      favouriteGif
  }

  def getActiveGifs(gifs: java.util.Set[String]) = {
    val results = gifRepository.findByIdAndActive(new util.LinkedList[String](gifs), true)
    logger.info(s"Results is ${results}")
    results
  }

  def getTrendingDetails(gifId: String) = {

    val gif = gifRepository.findOne(gifId)
    new GifTrendingData(gifId, gif.getShareCount, gif.getViewCount, gif.getFavCount)
  }

  def unMarkAsFaviourite(gifId: String, userId: String) = {
    var favouriteGif = faviourteGifRepository.findByEmailId(userId)
    if (favouriteGif != null) {
      favouriteGif.getGifs.remove(gifId)
      faviourteGifRepository.save(favouriteGif)
    }
    mongoTemplate.findAndModify(Query.query(Criteria.where("id").is(new ObjectId(gifId)).andOperator(Criteria.where("favCount").gt(0))), new Update().inc("favCount", -1), classOf[GifMetaData])
  }


  def gifViewed(id: String, emailIdMaybe: Option[String]) = {
    val calendar = Calendar.getInstance()
    val day = "hits.day_" + calendar.get(Calendar.DAY_OF_YEAR);
    val month = "hits.month_" + calendar.get(Calendar.MONTH);
    val week = "hits.week_" + calendar.get(Calendar.WEEK_OF_YEAR);
    val year = "hits.year_" + calendar.get(Calendar.YEAR);


    val trendingGif = trendingGifRepository.findOne(id)
    if (trendingGif == null)
      trendingGifRepository.save(new TrendingGif(id))
    mongoTemplate.updateFirst(Query.query(Criteria.where("id").is(new ObjectId(id))),
      new Update().inc(day, 1).inc(month, 1).inc(week, 1).inc(year, 1), classOf[TrendingGif])


    val gifMetaData = gifRepository.findOne(id)

    val gifHit = new GifHit()
    //    gifHit.setCategory(gifMetaData.getCategories)
    gifHit.setTags(gifMetaData.getTags)
    gifHit.setGifId(gifMetaData.getId)
    gifHit.setIdioms(gifMetaData.getIdioms)
    gifHit.setTimestamp(new Date())
    emailIdMaybe.map(e => gifHit.setEmailId(e))
    gifHitRepository.save(gifHit);
    mongoTemplate.findAndModify(Query.query(Criteria.where("id").is(new ObjectId(id))), new Update().inc("viewCount", 1), classOf[GifMetaData])
  }

  def getFieldName(trendingPeriod: TrendingPeriod) = {
    val calendar = Calendar.getInstance()
    trendingPeriod match {
      case TrendingPeriod.Daily => "day_" + calendar.get(Calendar.DAY_OF_YEAR);
      case TrendingPeriod.Weekly => "week_" + calendar.get(Calendar.WEEK_OF_YEAR);
      case TrendingPeriod.Monthly => "month_" + calendar.get(Calendar.MONTH);
      case TrendingPeriod.Yearly => "year_" + calendar.get(Calendar.YEAR);
    }
  }

  def getLanguagesAndTags(exclude: String) = {
    val excludes = new String(Base64.getDecoder.decode(exclude)).split("[,]")
    val languages = Idiom.values().map(_.toString.toLowerCase)
    val idioms = excludes.filter(part => languages.contains(part.toLowerCase())).map(part => Idiom.valueOf(part.capitalize)).toList
    val tags = excludes.filterNot(part => languages.contains(part.toLowerCase())).filter(_.trim.length > 0).map(_.replace("\"", "")).toList
    (idioms, tags)
  }

  def getGifs(primaryIdiom: String, filter: List[String], text: String, exclude: Option[String], pageIndex: Int, pageSize: Int) = {
    def getAggOperations(matchOp: DBObject, filterOp: DBObject, excludeLanguages: Option[DBObject],projections: DBObject, sort: DBObject, skip: DBObject, limit: DBObject) = {
      val operations = if (filter.length > 0)
        new util.LinkedList[DBObject](util.Arrays.asList(matchOp, filterOp, projections, sort, skip, limit))
      else
        new util.LinkedList[DBObject](util.Arrays.asList(matchOp, projections, sort, skip, limit))
      excludeLanguages.map(operations.add(1, _))
      operations
    }

    val excludes = exclude.map(getLanguagesAndTags(_))

    def getTotalCount(collection: DBCollection, operations: util.List[DBObject]) = {
      if (pageIndex == 0) {
        //val groupOp:DBObject = JSON.parse("{$group: {" + "_id: null, $count: {$sum:1}}}").asInstanceOf[DBObject]
        val countProject = JSON.parse("{$count: \"totalCount\"}").asInstanceOf[DBObject]


        /*val countProject = JSON.parse("{\"project\": \"count\":1}")*/
        val endInde = if (filter.length > 0) 2 else 1
        val countOperations: util.List[DBObject] = new util.LinkedList[DBObject](operations.subList(0, endInde))
        //scountOperations.add(groupOp)
        countOperations.add(countProject)
        val results = collection.aggregate(countOperations, ReadPreference.nearest()).results().asScala
        if (results.size > 0)
          mongoTemplate.getConverter.read(classOf[PageResponse[GifMetaData]], results.head).getTotalCount
        else
          0l
      } else
        0l
    }

    def getExcludeText(excludeText: List[String]) = {
      val excludeItems = excludeText.map(item => {
        val split = item.split(" ")

          split.map(col => " -" + col).mkString(" ")

      })
      excludeItems.mkString(" ").replace("\'", "\\'")
    }

    mongoTemplate.execute("gifMetaData", new CollectionCallback[PageResponse[GifMetaDataDto]] {
      override def doInCollection(collection: DBCollection): PageResponse[GifMetaDataDto] = {

        val excludeText = excludes.map(_._2).getOrElse(Nil)
        val excludeLangs = excludes.map(_._1).getOrElse(Nil)

        val searchText  = if(excludeText.size > 0 ) (text  + " " + getExcludeText(excludeText)) else text

        val matchOp = JSON.parse("{$match:{ $text: { $search: \'"+searchText+"\',  $diacriticSensitive: true}, \"active\": true}}").asInstanceOf[DBObject];

        logger.info(s"Match Operation is ${matchOp}")

        val excludeLangsText = excludeLangs.map("\"" +  _ + "\"").mkString(",")

        val excludeLanguages = if(excludeLangs.size > 0)
          Some(JSON.parse("{$match:{ \"idioms\": { $nin: ["+excludeLangsText+"]}}}").asInstanceOf[DBObject])
        else
          None

        logger.info(s"Exclude Language is ${excludeLanguages}")

        val filterIds = filter.map("{ \"$oid\":\""+ _ +"\"}").mkString(",")
        val filterOp = JSON.parse("{$match:{ _id: { $nin: ["+filterIds+"]}}}").asInstanceOf[DBObject];

        val projections = JSON.parse("{ \"$project\" : {\"_id\" : 1, \"originalFN\": 1, \"baseUrl\": 1, \"waterMarkedFN\": 1, \"idioms\" : 1,\"tags\" : 1,\"url\" : 1,\"publishedOn\" : 1," +
          "\"tags\" : 1,\"lowResFN\" : 1, \"lowResWebpFN\" : 1, \"thumbNailFN\" : 1,\"size\" : 1,\"source\" : 1,\"sha256\" : 1,\"publishedBy\" : 1,\"sourceUrl\" : 1,\"active\" : 1," +
          "\"width\" : 1,\"height\" : 1,\"lowResWidth\" : 1,\"lowResHeight\" : 1,\"lowResSize\" : 1,\"shareCount\" : 1," +
          "\"favCount\" : 1,\"viewCount\" : 1,\"triggerExpr\" : 1,\"order\" : {\"$cond\" : {if : { \"$in\" : [\"" + primaryIdiom + "\", \"$idioms\"] }, then : 1,else  : 2}}}}").asInstanceOf[DBObject];
      //  val projections = JSON.parse("{ \"$project\" : {\"_id\" : 1,\"idioms\" : 1,\"tags\" : 1,\"url\" : 1,\"publishedOn\" : 1,\"tags\" : 1,\"lowResUrl\" : 1,\"thumbNailUrl\" : 1,\"size\" : 1,\"source\" : 1,\"sha256\" : 1,\"publishedBy\" : 1,\"sourceUrl\" : 1,\"active\" : 1,\"width\" : 1,\"height\" : 1,\"lowResWidth\" : 1,\"lowResHeight\" : 1,\"lowResSize\" : 1,\"shareCount\" : 1,\"favCount\" : 1,\"viewCount\" : 1,\"triggerExpr\" : 1,\"order\" : 1}}}}").asInstanceOf[DBObject];

        val sort = JSON.parse("{\"$sort\" : {\"order\" : 1, \"score\": { $meta: \"textScore\" }, \"publishedOn\": -1 ,\"_id\": 1 }}").asInstanceOf[DBObject]
        val skip = JSON.parse("{\"$skip\" : " + pageIndex * pageSize + "}").asInstanceOf[DBObject]
        val limit = JSON.parse("{\"$limit\" : " + pageSize + "}").asInstanceOf[DBObject]

        val operations: util.List[DBObject] = getAggOperations(matchOp, filterOp, excludeLanguages, projections, sort, skip, limit)
        val item = collection.aggregate(operations, ReadPreference.nearest())
        val count = getTotalCount(collection, operations)

        //val count = gifRepository.countByTags(Pattern.compile(Pattern.quote(text), Pattern.CASE_INSENSITIVE))
        new PageResponse[GifMetaDataDto](count, item.results().asScala.map(u => {
          logger.info(s"Serialized data is ${JSON.serialize(u)}")
          mongoTemplate.getConverter.read(classOf[GifMetaData], u)
        }).map(mData => GifMetaDataConverter.convertToDto(mData)).toList.asJava)
      }
    })
  }

  def getTrendingGifs(idiom: String, tag: String, trendingPeriod: TrendingPeriod, count: Int) = {
    val sort = getSort(trendingPeriod)


    val aggregation = newAggregation(lookup("gifMetaData", "_id", "_id", "gifs"),
      `match`(Criteria.where("gifs.tags").is(tag).and("gifs.idiom").is(idiom)),
      sort,
      project("gifs.id", "gifs.url"),
      limit(count))
    val results = mongoTemplate.aggregate(aggregation, classOf[TrendingGif], classOf[TopItemData]).getMappedResults
    //mongoTemplate.executeCommand("")
    results
  }


  private def getSort(trendingPeriod: TrendingPeriod) = {
    new AggregationOperation {
      override def toDBObject(context: AggregationOperationContext) = {
        new BasicDBObject("$sort", new BasicDBObject("hits." + getFieldName(trendingPeriod), -1))
      }
    }
  }

  def getTopItem(idiom: String, tag: String, trendingPeriod: TrendingPeriod) = {
    val sort = getSort(trendingPeriod)

    val aggregation = newAggregation(lookup("gifMetaData", "_id", "_id", "gifs"),
      `match`(Criteria.where("gifs.tags").is(tag).andOperator(Criteria.where("gifs.idiom").is(idiom))),
      sort,
      project("gifs.id", "gifs.url"),
      limit(1))
    val results = mongoTemplate.aggregate(aggregation, classOf[TrendingGif], classOf[TopItem]).getMappedResults
    if (!results.isEmpty) {
      Some(results.get(0))
    } else {
      None
    }

  }

  def searchGif(filter: SearchFilter, page: Int, size: Int) = {
    import org.springframework.data.mongodb.core.query.Query
    import org.springframework.data.mongodb.core.query.TextCriteria
    import org.springframework.data.mongodb.core.query.TextQuery
    def getQuery = {
      if(filter.getTag != null && filter.getTag.trim.length > 0) {
        var criteria: TextCriteria = TextCriteria.forDefaultLanguage
        criteria.matchingAny(filter.getTag)
        TextQuery.queryText(criteria).sortByScore
      }else{
        val query = new Query()
        query.`with`(new Sort(Sort.Direction.DESC, "publishedOn"));
      }

    }

    val query = getQuery
   /* var criteria: TextCriteria = getQueryResult._1
    val query: TextQuery = getQueryResult._2*/

    /*var criteria: TextCriteria = new Criteria();
    criteria*/
    if (!filter.isTagsPresent)
      query.addCriteria(Criteria.where("this.tags.length").is(0))


    if (filter.getSource != null)
      query.addCriteria(Criteria.where("source").is(filter.getSource))
      //criteria = criteria.and("source").is(filter.getSource)

    if(filter.getPublishedBy!=null)
      query.addCriteria(Criteria.where("publishedBy").is(filter.getPublishedBy))
      //criteria=criteria.and("publishedBy").is(filter.getPublishedBy)

    if (filter.getIdiom != null)
      query.addCriteria(Criteria.where("idioms").is(filter.getIdiom.toString))

    if (filter.getTagCorrectionRequired == BooleanSearchCriteria.TRUE)
      query.addCriteria(new Criteria().orOperator(Criteria.where("tagCorrected").is(false), Criteria.where("tagCorrected").exists(false)))
    else if (filter.getTagCorrectionRequired == BooleanSearchCriteria.FALSE)
      query.addCriteria(Criteria.where("tagCorrected").is(true))

    /*if (filter.getTag != null && filter.getTag.length > 0)
      criteria = criteria.andOperator(TextCriteria.forDefaultLanguage().matching(filter.getTag))*/

    /*val query = new Query(criteria);*/
    query.skip(page * size)
    query.limit(size)

    val results = mongoTemplate.find(query, classOf[GifMetaData])
    new PageImpl[GifMetaData](results, new PageRequest(page, size), mongoTemplate.count(query, classOf[GifMetaData]))


    /*if(!filter.isTagsPresent && filter.getSource == null  ){

      gifRepository.findGifWithNoTags(filter.getIdiom, new PageRequest(page, size))
    }else if(!filter.isTagsPresent && filter.getSource != null){
      gifRepository.findGifWithNoTagsAndSource(filter.getIdiom, filter.getSource, new PageRequest(page, size))
    }
    else if(filter.isTagsPresent &&  filter.getTag == null && filter.getSource != null){
      gifRepository.findByIdiomAndSource(filter.getIdiom,  true, filter.getSource, new PageRequest(page, size))
    }
    else if(filter.isTagsPresent && filter.getSource != null ){
      gifRepository.findByIdiomAndTextAndSource(filter.getIdiom, Pattern.compile(Pattern.quote(filter.getTag), Pattern.CASE_INSENSITIVE), true, filter.getSource, new PageRequest(page, size))
    }
    else if(filter.isTagsPresent && filter.getSource == null){
      gifRepository.findByIdiomAndText(filter.getIdiom.toString, Pattern.compile(Pattern.quote(filter.getTag), Pattern.CASE_INSENSITIVE), true, new PageRequest(page, size))
    }
    else{
      throw new RuntimeException("unknown criteria")
    }*/
  }

  def gifSearched(idiom: String, text: String, gifId: String): Unit = {
    val searchHit = new SearchHit()
    searchHit.setIdiom(idiom)
    searchHit.setText(text)
    searchHit.setGifId(gifId)
    searchHit.setActive(true)
    searchHit.setTimestamp(new Date())
    searchHitRepository.save(searchHit)
  }

  def getMills(trendingPeriod: TrendingPeriod): Long = {
    trendingPeriod match {
      case TrendingPeriod.Daily => 24l * 60l * 60l * 1000l
      case TrendingPeriod.Weekly => 24l * 60l * 60l * 1000l * 7l
      case TrendingPeriod.Monthly => 24l * 60l * 60l * 1000l * 30l
      case TrendingPeriod.Yearly => 24l * 60l * 60l * 1000l * 365l
    }
  }

  def getGifWithNoTags(idiom: Idiom, page: Int, pageSize: Int) = {
    gifRepository.findGifWithNoTags(idiom, new PageRequest(page, pageSize, sortByPublishedOn()))
  }

  def getGifWithNoTags(publishedBy: String, idiom: Idiom, page: Int, pageSize: Int) = {
    gifRepository.findGifWithNoTags(idiom, publishedBy, new PageRequest(page, pageSize, sortByPublishedOn()))
  }

  def getTopSearches(idiom: String, trendingPeriod: TrendingPeriod, page: Int, size: Int): List[TopItem] = {
    val calendar = Calendar.getInstance()
    val trendingPeriodMillis = getMills(trendingPeriod)
    val calculatedTrendingMillis = calendar.getTimeInMillis - trendingPeriodMillis
    calendar.setTimeInMillis(calculatedTrendingMillis)
    logger.info(s"Trending period millis is ${trendingPeriodMillis}, calculatedMillis is ${calculatedTrendingMillis} and thre date is ${calendar.getTime}")


    val groupby = group("text").count().as("hitCount")
    val matcher = `match`(Criteria.where("idiom").is(idiom).and("timestamp").gte(calendar.getTime).and("active").is(true))
    val projectAs = project("hitCount").and("text").previousOperation()
    val skipTill = skip(page * size)
    val sortUsing = sort(Sort.Direction.DESC, "hitCount")

    val aggregation = newAggregation(matcher, groupby, projectAs, sortUsing, limit(size), skipTill)
    val results = mongoTemplate.aggregate(aggregation, classOf[SearchHit], classOf[TopItem]).getMappedResults
    results.asScala.map(ts => {
      val searchHit = searchHitRepository.findByTextOrderByTimestampDesc(ts.getText)
      if (searchHit.getGifId != null) {
        val gif = GifMetaDataConverter.convertToDto(gifRepository.findOne(searchHit.getGifId))
        if (gif != null) {
          ts.setGifId(gif.getId)
          ts.setBaseUrl(gif.getBaseUrl)
          ts.setOriginalFN(gif.getOriginalFN)
          ts.setThumbNailFN(gif.getThumbNailFN)
          ts.setLowResFN(gif.getLowResFN)
          ts.setLowResWebpFN(gif.getLowResWebpFN)
        }
      }
      ts
    }).toList
  }

  def getGifIdiomCountStats(idiom: String) = {
    val activeCount = mongoTemplate.count(new Query(Criteria.where("active").is(true).and("idioms").is(idiom)), classOf[GifMetaData]);
    val taggingPending = mongoTemplate.count(new Query(new Criteria().orOperator(Criteria.where("tagCorrected").exists(false), Criteria.where("tagCorrected").is(false)).and("idioms").is(idiom)), classOf[GifMetaData]);
    mongoTemplate.find(new Query(new Criteria().orOperator(Criteria.where("tagCorrected").exists(false), Criteria.where("tagCorrected").is(false)).and("idioms").is(idiom)), classOf[GifMetaData]);
    val totalCount = mongoTemplate.count(new Query(Criteria.where("idioms").is(idiom)), classOf[GifMetaData]);
    new GifIdiomStat(idiom, totalCount, activeCount, taggingPending)
  }

  def getGifCountStats() = {
    val idiomCountStats = Idiom.values().map(i => getGifIdiomCountStats(i.toString())).toList.asJava
    val totalCount = gifRepository.count()
    new GifCountStats(idiomCountStats, totalCount)
  }

  def getGifTrendingStats() = {
    val idiomCountStats = Idiom.values().map(i => {
      val topSearches = TrendingPeriod.values().map(t =>
        t -> getTopSearches(i.toString(), t, 0, 20).asJava).toMap[TrendingPeriod, java.util.List[TopItem]].asJava

      val topShares = TrendingPeriod.values().map(t =>
        t -> getTopItems(i.toString(), t, 0, 20).asJava).toMap[TrendingPeriod, java.util.List[TopItem]].asJava

      new GifIdiomTrendingStats(i.toString, topSearches, topShares)
    }).toList.asJava
    new GifTrendingStats(idiomCountStats)
  }


  def getTopItems(idiom: String, trendingPeriod: TrendingPeriod, from: Int, size: Int): List[TopItem] = {
    val calendar = Calendar.getInstance()
    calendar.setTimeInMillis(System.currentTimeMillis() - getMills(trendingPeriod))

    val groupby = group("gifId").count().as("hitCount")
    val matcher = `match`(Criteria.where("idioms").is(idiom).and("timestamp").gte(calendar.getTime).and("active").is(true))
    val projectAs = project("hitCount").and("gifId").previousOperation()
    val sortUsing = sort(Sort.Direction.DESC, "hitCount")


    val aggregation = newAggregation(matcher, groupby, projectAs, sortUsing, limit(size), skip(from * size))
    val results = mongoTemplate.aggregate(aggregation, classOf[GifShare], classOf[TopItem]).getMappedResults
    results.asScala.map(ts => {
      val gif = gifRepository.findOne(ts.getGifId)
      ts.setBaseUrl(gif.getBaseUrl)
      ts.setOriginalFN(gif.getOriginalFN)
      val tag = if (gif.getTags.size() > 0) gif.getTags.get(0) else ""
      ts.setText(tag)
      ts
    }).toList
  }

  def getTopShare(idiom: String, tag: String, trendingPeriod: TrendingPeriod): Option[TopItem] = {
    val calendar = Calendar.getInstance()

    calendar.setTimeInMillis(System.currentTimeMillis() - getMills(trendingPeriod))

    val groupby = group("gifId").count().as("hitCount")
    val matcher = `match`(Criteria.where("idiom").is(idiom).andOperator(Criteria.where("timestamp").gte(calendar.getTime), Criteria.where("tags").is(tag), Criteria.where("active").is(true)))
    val projectAs = project("hitCount").and("gifId").as("id")
    val sortUsing = sort(Sort.Direction.DESC, "hitCount")


    val aggregation = newAggregation(matcher, groupby, projectAs, sortUsing, limit(1))
    logger.info("executing aggregation query " + aggregation.toString)
    val results = mongoTemplate.aggregate(aggregation, classOf[GifShare], classOf[TopItem]).getMappedResults
    logger.info("top searches result is " + results)
    results.asScala.map(ts => {
      val gif = GifMetaDataConverter.convertToDto(gifRepository.findOne(ts.getGifId), null, None)
      if (gif.getActive) {
        ts.setBaseUrl(gif.getBaseUrl)
        ts.setOriginalFN(gif.getOriginalFN)
        ts.setThumbNailFN(gif.getThumbNailFN)
        ts.setLowResFN(gif.getLowResFN)
        ts.setLowResWebpFN(gif.getLowResWebpFN)
      }
      ts.setText(tag)
      ts
    }).headOption
  }

  def getGifViewedBy(emailId: String) = {
    val gifViewList = gifHitRepository.findByEmailId(emailId).asScala.map(_.getGifId).asJava
    gifRepository.findById(gifViewList)
  }

  def getGifPublishedBy(emailId: String) = {
    gifRepository.findByPublishedBy(emailId, sortByPublishedOn())
  }

  def getGifPublishedByForIdiom(emailId: String, idiom: String, page: Int, size: Int) = {
    gifRepository.findByPublishedByAndIdioms(emailId, idiom, new PageRequest(page, size, sortByPublishedOn()))
  }

  private def sortByPublishedOn() = {
    new Sort(Sort.Direction.DESC, "publishedOn")
  }

  private def sortByTimestamp() = {
    new Sort(Sort.Direction.DESC, "timestamp")
  }

  def getGifPublishedBy(emailId: String, page: Int, size: Int) = {
    gifRepository.findByPublishedBy(emailId, new PageRequest(page, size, sortByPublishedOn()))
  }


  def getTagAuditDetails(emailId: String, page: Int, size: Int): GifTagCountDetail = {

    def getTagAuditDetail(item: (String, mutable.Buffer[TagUpdateAudit])): GifTagDetail = {
      val gifId = item._1
      val tagAudits = item._2
      val gif = getGifById(gifId);
      val added = tagAudits.flatMap(_.getTagsAdded.asScala).toSet[String]
      val removed = tagAudits.flatMap(_.getTagsRemoved.asScala).toSet[String]
      var tagDetail = new GifTagDetail(gifId, gif.getBaseUrl + "/" + gif.getWaterMarkedFN, gif.getTags, gif.getIdioms, gif.getSize);
      tagDetail.setAddedTags(added.toList.asJava);
      tagDetail.setRemovedTags(removed.toList.asJava);
      tagDetail
    }

    def getTaggerAuditDetailsCount: Int = {
      val groupby = group("taggerId").addToSet("gifId").as("gifArray")
      val matcher = `match`(Criteria.where("taggerId").is(emailId))
      val projectAs = project("taggerId").and("gifArray").size().as("gifCount")
      val aggregation = newAggregation(matcher, groupby, projectAs)
      val results = mongoTemplate.aggregate(aggregation, classOf[TagUpdateAudit], classOf[TaggerDetailCount]).getMappedResults
      var count = 0;
      if (!results.isEmpty)
        count = results.get(0).getGifCount;
      count;
    }

    def getGifIdListByTaggerId(): util.List[TaggerDetailCount] = {
      val limitValue = size.toLong
      val skipValue = (page * size)
      val groupby = group("gifId")
      val matcher = `match`(Criteria.where("taggerId").is(emailId))
      val projectAs = project().and("gifId").previousOperation()
      val aggregation = newAggregation(matcher, groupby, projectAs, skip(skipValue), limit(limitValue))
      val results = mongoTemplate.aggregate(aggregation, classOf[TagUpdateAudit], classOf[TaggerDetailCount]).getMappedResults
      results
    }

    var tagAuditDetails: java.util.List[GifTagDetail] = new util.LinkedList[GifTagDetail]()

    val results = getGifIdListByTaggerId()
    if (!results.isEmpty) {
      var gifIdList = results.asScala.map(item => {
        item.getGifId
      }).toList;

      var tagAuditDetailList = tagUpdateAuditRepository.findByGifIdIn(gifIdList.asJava)

      val tagUpdateListByGifId = tagAuditDetailList.asScala.groupBy(_.getGifId)
      tagAuditDetails = tagUpdateListByGifId.map(getTagAuditDetail(_)).toList.asJava

    }

    new GifTagCountDetail(tagAuditDetails, getTaggerAuditDetailsCount.toLong);

    /* val tagUpdates = tagUpdateAuditRepository.findByTaggerId(emailId, new PageRequest(page, size, sortByTimestamp()));
     val tagUpdateAuditList = tagUpdates.getContent.asScala;
     val tagUpdateListByGifId = tagUpdateAuditList.groupBy(_.getGifId)
     val tagAuditDetails= tagUpdateListByGifId.map(getTagAuditDetail(_)).toList.asJava*/


  }


  def getAllTags() = {
    val allTagses = tagsRepository.findAll()
    allTagses.get(0)

  }

  def saveCategoriesAndTags(mapping: String) = {
    val categoryAndTagsList = mapping.split("[\\r\\n]+").map(line => {
      val parts = line.split(",")
      val toList = parts.toList
      val category = toList.head
      val tags = toList.tail
      new CategoryTags(category, tags.asJava)
    }).toList.asJava

    categoryTagsRepository.save(categoryAndTagsList)
  }

  def getCategoriesAndTags() = {
    categoryTagsRepository.findAll()
  }

  def getGifById(gifId: String) = {
    gifRepository.findOne(gifId)
  }

  def getGifByIdList(gifIdList: util.List[String]) = {

    val gifs: util.List[GifMetaData] = gifRepository.findByIdIn(gifIdList)
    gifs


  }

  def processGifUploadRequest(currentUser: String, bytes: Array[Byte],
                              categories: List[String], tags: List[String],
                              idiom: List[String], userFinder: UserService, s3Uploader: S3Uploader, cloudFrontUrl: String, lowResGifActor: ActorRef,
                              active: Boolean = false, source: String = "Gola") = {

    /*val categories =
    val tags =
    val idioms = mp.dataParts.get("idiom").get
    val idiom = idioms(0);*/

    val user = userFinder.findUserByEmailId(currentUser).get
    val checksum = MessageDigest.getInstance("SHA-256") digest bytes
    val computedSha256 = checksum.map("%02X" format _).mkString

    /*  val user = request.headers.get("currentUser").get*/

    saveGif(categories, tags, idiom, user, bytes, computedSha256, s3Uploader, cloudFrontUrl, lowResGifActor, source, active)


  }

  private def saveGif(categories: List[String], tags: List[String], idiom: List[String], user: User,
                      bytes: Array[Byte], computedSha256: String, s3Uploader: S3Uploader, cloudFrontUrl: String, lowResGifActor: ActorRef, source: String = "Gola", active: Boolean) = {
    import collection.JavaConverters._
    val sha256 = findBySha256(computedSha256)
    if (sha256 == null) {
      val outputStream = new ByteArrayOutputStream()

      val uuid: String = BSONObjectID.generate().stringify
      val originalFN = uuid + ".gif"
      val partnerPrefix = Base64.getEncoder.encodeToString(user.getPartnerId.getBytes)
      val destFilePath = partnerPrefix + "/" + originalFN


      s3Uploader.putFile(bytes, destFilePath);



      val baseUrl = cloudFrontUrl + "/" + partnerPrefix

      val gifMetaData = new GifMetaData(uuid, baseUrl, originalFN, new Date(), tags.asJava,
        idiom.map(Idiom.valueOf(_)).toList.asJava, 0, computedSha256, user.getEmailId, active)

      try {
        val imageDimension = new ThumbNailer().transform(1, outputStream, new ByteArrayInputStream(bytes))
        gifMetaData.setWidth(imageDimension.getWidth.toInt)
        gifMetaData.setHeight(imageDimension.getHeight.toInt)
        gifMetaData.setSource(source)
        val thumbNailFN = uuid + "thumbnail.jpg"
        val thumbNailFilePath = partnerPrefix + "/" + thumbNailFN
        s3Uploader.putFile(outputStream.toByteArray, thumbNailFilePath);
        val thumbNailUrl = cloudFrontUrl + "/" + thumbNailFilePath

        gifMetaData.setThumbNailFN(thumbNailFN)
        //generateAndUpdateLowResGif(user, bytes, cloudFrontUrl, gifMetaData)
      } catch {
        case e: ArrayIndexOutOfBoundsException =>
          logger.warn("Error when generating thumbnail image", e);
      }
      gifMetaData.setSize(bytes.length)
      val savedData = saveGifMetaData(gifMetaData)


      saveGifConversionStatus(savedData)

      //lowResGifActor ! GenerateLowResGifFromBytes(uuid, bytes)
      gifMetaData
    } else
      throw new GifAlreadyExistsException()
  }


  private def saveGifConversionStatus(savedData: GifMetaData) = {
    var gifUrlStatus = new GifConversion()
    gifUrlStatus.setGifId(savedData.getId)

    gifUrlStatus.setLowResGifConversionStatus(GifConversionStatus.Pending)
    gifUrlStatus.setLowResWebpGifConversionStatus(GifConversionStatus.Pending)
    gifUrlStatus.setWaterMarkedGifConversionStatus(GifConversionStatus.Pending)
    gifConversionRepository.save(gifUrlStatus)
  }

  def convertGifToNewFormat() = {
    curatedTabRepository.findAll().asScala.foreach(meta => {
      curatedTabRepository.save(GifMetaDataConverter.convertToNewFormat(meta, gifRepository))
      logger.info(s"converted ${meta.getId}")
    })
  }

  def hasFailureForGif(gifId: String) = {
    val failure = lowResGifGenerationRepository.findByGifId(gifId)
    (failure != null);
  }

  def save(lowResGifGenerationFailure: LowResGifGenerationFailure) = {
    lowResGifGenerationRepository.save(lowResGifGenerationFailure)
  }

  def findTbdTags(text: String): util.List[String] = {
    val tags = findSuggestions(text);
    tags.asScala.filter(tag => tag.startsWith(text)).toList.asJava
  }

  def updateLowResData(gifId: String, width: Int, height: Int, size: Int, lowResFN: String) = {
    mongoTemplate.updateFirst(new Query(Criteria.where("id").is(new ObjectId(gifId))), new Update()
      .set("lowResWidth", width)
      .set("lowResHeight", height)
      .set("lowResFN", lowResFN)
      .set("lowResSize", size)
      , classOf[GifMetaData])
    var gifConversion = gifConversionRepository.findByGifId(gifId);
    if (gifConversion == null) {
      gifConversion = new GifConversion(gifId)
      gifConversion.setLowResGifConversionStatus(GifConversionStatus.Success)
      gifConversionRepository.save(gifConversion)
    } else {
      updateLowResGifConversionStatus(gifId, GifConversionStatus.Success)
    }
  }

  def updateLowResGifConversionStatus(gifId: String, gifConversionStatus: GifConversionStatus) = {
    mongoTemplate.updateFirst(new Query(Criteria.where("gifId").is(gifId)), new Update()
      .set("lowResGifConversionStatus", gifConversionStatus)
      , classOf[GifConversion])
  }


  def updateLowResWebpGifConversionStatus(gifId: String, gifConversionStatus: GifConversionStatus) = {
    mongoTemplate.updateFirst(new Query(Criteria.where("gifId").is(gifId)), new Update()
      .set("lowResWebpGifConversionStatus", gifConversionStatus)
      , classOf[GifConversion])
  }


  def updateWaterMakrStatus(gifId: String, gifConversionStatus: GifConversionStatus) = {
    mongoTemplate.updateFirst(new Query(Criteria.where("gifId").is(gifId)), new Update()
      .set("waterMarkedGifConversionStatus", gifConversionStatus)
      , classOf[GifConversion])
  }



  def updateLowResWebpUrl(gifId: String, lowResWebFN: String) = {
    mongoTemplate.updateFirst(new Query(Criteria.where("id").is(new ObjectId(gifId))),  new Update()
      .set("lowResWebpFN", lowResWebFN)
      , classOf[GifMetaData])
    var gifConversion = gifConversionRepository.findByGifId(gifId);
    if(gifConversion == null) {
      gifConversion = new GifConversion(gifId)
      gifConversion.setLowResWebpGifConversionStatus(GifConversionStatus.Success)
      gifConversionRepository.save(gifConversion)
    }else{
      updateLowResWebpGifConversionStatus(gifId, GifConversionStatus.Success)
    }
  }

  def updateWaterMarkedUrl(gifId: String, waterMarkedFN: String) = {
    mongoTemplate.updateFirst(new Query(Criteria.where("id").is(new ObjectId(gifId))),  new Update()
      .set("waterMarkedFN", waterMarkedFN)
      , classOf[GifMetaData])
    var gifConversion = gifConversionRepository.findByGifId(gifId);
    if(gifConversion == null) {
      gifConversion = new GifConversion(gifId)
      gifConversion.setWaterMarkedGifConversionStatus(GifConversionStatus.Success)
      gifConversionRepository.save(gifConversion)
    }else{
      updateWaterMakrStatus(gifId, GifConversionStatus.Success)
    }
  }


  def updateGifUrlStatus() = {
    def getStatus(hasUrl: Boolean) = {
      if (hasUrl)
        GifConversionStatus.Success
      else
        GifConversionStatus.Pending
    }

    val gifMetaDatas = gifRepository.findAll()
    val gifUrlStatusList: List[GifConversion] = gifMetaDatas.asScala.map(gif => {
      var gifUrlStatus = gifConversionRepository.findByGifId(gif.getId)
      if (gifUrlStatus == null) {
        gifUrlStatus = new GifConversion()
        gifUrlStatus.setGifId(gif.getId)
      }
      gifUrlStatus.setLowResGifConversionStatus(getStatus(gif.hasLowRes))
      gifUrlStatus.setLowResWebpGifConversionStatus(getStatus(gif.hasLowResWebp))
      gifUrlStatus.setWaterMarkedGifConversionStatus(getStatus(gif.isWaterMarked))
      gifUrlStatus.setThumbNailGifConversionStatus(getStatus(gif.hasThumbNail))
      gifUrlStatus
      gifConversionRepository.save(gifUrlStatus)
    }).toList

  }

 /* def markLowResGifAsPending()=
  {
    def getStatus(hasUrl:Boolean)=
    {
      if(hasUrl)
        GifConversionStatus.Success
      else
        GifConversionStatus.Pending
    }

    val gifMetaDatas = gifRepository.findAll()
    gifMetaDatas.asScala.foreach(gif => {
      var gifUrlStatus = gifConversionRepository.findByGifId(gif.getId)
      if(gif.getLowResHeight <= (gif.getHeight * .70)){
        logger.info("Url Conversion status set to pending for", gif.getId)
        gifUrlStatus.setLowResGifConversionStatus(GifConversionStatus.Pending)
        gifConversionRepository.save(gifUrlStatus)
      }
      gifUrlStatus
    })
    //gifConversionRepository.save(gifUrlStatusList.asJava)
  }*/

  def markLowResGifAsPending()=
  {
    def getStatus(hasUrl:Boolean)=
    {
      if(hasUrl)
        GifConversionStatus.Success
      else
        GifConversionStatus.Pending
    }

    val gifMetaDatas = gifRepository.findAll()
    val gifUrlStatusList:List[GifConversion]=gifMetaDatas.asScala.map(gif => {
      var gifUrlStatus = gifConversionRepository.findByGifId(gif.getId)
      if(gif.getLowResHeight < gif.getHeight * .70){
        gifUrlStatus.setLowResGifConversionStatus(GifConversionStatus.Pending)
      }
      gifUrlStatus
    }).toList
    gifConversionRepository.save(gifUrlStatusList.asJava)
  }



  def findBylowResUrlByPendingStatus()=
  {
    gifConversionRepository.findByLowResGifConversionStatus(GifConversionStatus.Pending)
  }

  def findBylowResUrlWebpByPendingStatus()=
  {
    gifConversionRepository.findByLowResWebpGifConversionStatus(GifConversionStatus.Pending)
  }

  def findByWaterMarkedUrlPendingStatus()=
  {
    gifConversionRepository.findByWaterMarkedGifConversionStatus(GifConversionStatus.Pending)
  }


  def createSearchTagsInGifMetaData() = {

    val gifCount = gifRepository.count()
    val totalPages = gifCount/1000
    for(page<-0 to totalPages.toInt){
      var gifMetaDatas = gifRepository.findAll(new PageRequest(page, 1000, sortByPublishedOn())).getContent
      gifMetaDatas = gifMetaDatas.asScala.map(gif => {
        val searchTags = gif.getTags.asScala.map(tag => {
          tag.replaceAll(" ", "")
        }).toList

        var tags=new util.LinkedList(searchTags.asJava)
        tags.addAll(gif.getTags)
        gif.setSearchTags(tags)

        gif
      }).toList.asJava
     gifRepository.save(gifMetaDatas)
    }
  }


}



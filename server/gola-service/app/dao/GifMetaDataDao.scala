package dao

import javax.inject.Inject

import model.GifMetaData
import dto.DtoSerializers._
import play.api.libs.json.{JsObject, Json}
import play.modules.reactivemongo.json.ImplicitBSONHandlers._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.api.ReadPreference
import reactivemongo.api.commands.UpdateWriteResult
import reactivemongo.bson.{BSONDocument, BSONRegex}
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Awaitable, Future}
import scala.util.{Failure, Success, Try}
import dao.MongoImplicits._
import dto.Suggestion
import model.JsonFormats._

/**
  * Created by senthil
  */

class GifMetaDataDao @Inject()(val reactiveMongoApi: ReactiveMongoApi) extends MongoController
  with ReactiveMongoComponents with GingerDao[GifMetaData] {

  /*def gifCollection: Future[JSONCollection] = database.map {
    _.collection[JSONCollection]("gifMetaData")
  }

  def saveGifMetadata(gifMetaData: GifMetaData): Future[Try[Boolean]] = {

    gifCollection.map(coll => {

      val updateWriteResult: UpdateWriteResult = getResult(coll.update(Json.obj("_id" -> gifMetaData._id), gifMetaData, upsert = true))
      val update: Try[Boolean] = updateWriteResult
      update
    }
    )
  }

  def getGifForIdiom(idiom: String): Future[List[JsObject]] = {
    gifCollection.map(
      findByIdiom(idiom, _)
    )
  }

  def getGifForIdiomAndCategory(idiom: String, category: String): Future[List[JsObject]] = {
    gifCollection.map {
      findByIdiomAndCategory(idiom, category, _)
    }
  }

  def getSuggestions(idiom: String, text: String): Set[String] = {
    val result:Set[Suggestion] = getResult(gifCollection.map { collection => findSuggestions(idiom, text, collection)})
    result.map(s => s.tags ++ s.categories).flatten.filter(_.startsWith(text))
  }


  private def findSuggestions(idiom: String, text: String, collection: JSONCollection) = {

    val items:List[BSONDocument] = BSONDocument("categories" -> BSONDocument("$regex" -> BSONRegex("^" + text, "i"))) :: BSONDocument("tags" -> BSONDocument("$regex" -> BSONRegex("^" + text, "i"))) :: Nil;
    val query:BSONDocument = BSONDocument("$or" -> items)
    val andQuery = BSONDocument("idiom" -> idiom) :: query :: Nil
    val idiomQuery = BSONDocument("$and" -> andQuery)
    getResult(
      collection.find(idiomQuery, BSONDocument("tags" -> 1, "categories" -> 1)).cursor[Suggestion](ReadPreference.nearest).collect[Set]()
    )

  }

  def getGifForIdiomAndText(idiom: String, text: String): Future[List[JsObject]] = {
    gifCollection.map {
      findByIdiomAndText(idiom, text, _)
    }
  }

  def updateTagsAndCategory(id: String, categories: List[String], tags: List[String]): Try[Boolean] = {

    val awaitable = gifCollection.map { coll =>
      processMetaDataUpdate(id, tags, categories, coll)
    }
    getResult(awaitable)
  }

  def updateTags(id: String, tags: List[String]): Try[Boolean] = {
    def saveGif(gif: GifMetaData): Try[Boolean] = {
      val newGif = gif.newTags(tags)
      getResult(saveGifMetadata(newGif))
    }

    val eventualMaybeData: Option[GifMetaData] = getResult(gifCollection.flatMap(_.find(Json.obj("_id" -> id)).one[GifMetaData]))
    if (eventualMaybeData.isEmpty)
      Failure(new RuntimeException("Gif not found"))
    else {
      saveGif(eventualMaybeData.get)
    }
  }

  def updateCategories(id: String, categories: List[String]): Try[Boolean] = {
    def saveGif(gif: GifMetaData): Try[Boolean] = {
      val newGif = gif.newCategories(categories)
      getResult(saveGifMetadata(newGif))
    }

    val eventualMaybeData: Option[GifMetaData] = getResult(gifCollection.flatMap(_.find(Json.obj("_id" -> id)).one[GifMetaData]))
    if (eventualMaybeData.isEmpty)
      Failure(new RuntimeException("Gif not found"))
    else {
      saveGif(eventualMaybeData.get)
    }
  }

  private def processMetaDataUpdate(id: String, categories: List[String], tags: List[String], coll: JSONCollection): Try[Boolean] = {
    def saveGif(gif: GifMetaData): Try[Boolean] = {
      val newGif = gif.newCategories(categories).newTags(tags)
      getResult(saveGifMetadata(newGif))
    }

    val eventualMaybeData: Option[GifMetaData] = getResult(coll.find(Json.obj("_id" -> id)).one[GifMetaData])
    if (eventualMaybeData.isEmpty)
      Failure(new RuntimeException("Gif not found"))
    else
      saveGif(eventualMaybeData.get)
  }

  def findByIdiom(idiom: String, collection: JSONCollection) = {
    getResult(
      collection.find(Json.obj("idiom" -> idiom)).cursor[JsObject](ReadPreference.nearest).collect[List]()
    )
  }

  def findBySha256(sha256: String): Option[GifMetaData] = {
    val future = gifCollection.flatMap {
      _.find(Json.obj("sha256" -> sha256)).one[GifMetaData]
    }

    getResult(
      future
    )
  }

  def findById(id: String): Option[GifMetaData] = {
    val future = gifCollection.flatMap {
      _.find(Json.obj("_id" -> id)).one[GifMetaData]
    }

    getResult(
      future
    )
  }


  def findByIdiomAndCategory(idiom: String, category: String, collection: JSONCollection) = {
    getResult(
      collection.find(Json.obj("idiom" -> idiom, "categories" -> category)).cursor[JsObject](ReadPreference.nearest).collect[List]()
    )
  }

  def findByIdiomAndText(idiom: String, text: String, collection: JSONCollection) = {
    val items:List[BSONDocument] = BSONDocument("categories" -> BSONDocument("$regex" -> BSONRegex("^" + text, "i"))) :: BSONDocument("tags" -> BSONDocument("$regex" -> BSONRegex("^" + text, "i"))) :: Nil;
    val query:BSONDocument = BSONDocument("$or" -> items)
    val andQuery = BSONDocument("idiom" -> idiom) :: query :: Nil
    val idiomQuery = BSONDocument("$and" -> andQuery)
    getResult(
      collection.find(idiomQuery).cursor[JsObject](ReadPreference.nearest).collect[List]()
    )
  }*/

}

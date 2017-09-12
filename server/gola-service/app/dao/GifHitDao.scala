package dao

import java.util.Calendar
import javax.inject.Inject

import dao.MongoImplicits._
import dto.DtoSerializers._
import dto.Suggestion
import model.{GifHit, GifMetaData, TrendingGif}
import model.JsonFormats._
import play.api.libs.json.{JsObject, Json}
import play.modules.reactivemongo.json.ImplicitBSONHandlers._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import play.modules.reactivemongo.json._
import ImplicitBSONHandlers._
import reactivemongo.api.ReadPreference
import reactivemongo.api.commands.UpdateWriteResult
import reactivemongo.bson.BSONDocument
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

/**
  * Created by senthil
  */

class GifHitDao @Inject()(val reactiveMongoApi: ReactiveMongoApi) extends MongoController
  with ReactiveMongoComponents with GingerDao[GifHit] {

  def hitCollection:JSONCollection = getResult(database.map {
    _.collection[JSONCollection]("gifHit")
  })

  def trendingCollection:JSONCollection = getResult(database.map {
    _.collection[JSONCollection]("trendingGif")
  })

  def saveNewHit(hit: GifHit) = {
    //val hits:UpdateWriteResult = getResult(hitCollection.update(Json.obj("_id" -> 1), hit, upsert = true))
  }

  def saveNewHit(trending: TrendingGif):Try[Boolean] = {

    /*val cal = Calendar.getInstance()
    val dayOfYear = cal.get(Calendar.DAY_OF_YEAR);
    val monthOfYear = cal.get(Calendar.MONTH);
    val weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);

    val day = "day_" + dayOfYear
    val month = "month_" + monthOfYear
    val week = "week_" + monthOfYear
    val update = BSONDocument("$inc" -> BSONDocument(day -> 1)) :: BSONDocument("$inc" -> BSONDocument(month -> 1)) :: BSONDocument("$inc" -> BSONDocument(week -> 1)) :: Nil

    import predef.Predef._
    val result:Try[Boolean] = trendingCollection.update(Json.obj("_id" -> trending._id),update, upsert = true).getResult
    result*/
    Success(true)
  }
}

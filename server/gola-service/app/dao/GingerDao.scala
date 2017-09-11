package dao

import model.User
import play.api.libs.json.{JsObject, Json}
import reactivemongo.api.commands.UpdateWriteResult

import scala.concurrent.{Await, Awaitable}
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success, Try}

/**
  * Created by senthil
  */
trait GingerDao[T] {

  def getResult[K](awaitable: Awaitable[K]) = {
    Await.result(awaitable, Duration.Inf)
  }



  /*def toDomain(jsObject:JsObject):T = {
    Json.fromJson[T](jsObject).get
  }*/

}

object MongoImplicits{
  implicit def writeResultToTry(writeResult: UpdateWriteResult):Try[Boolean] = {
    println(writeResult.writeErrors)
    if(writeResult.writeErrors.isEmpty)
      Success(true)
    else
      Failure(new RuntimeException("Error when writing data to Mongo "))
  }
}

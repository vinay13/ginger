package dao

import javax.inject.Inject

import model.{User, UserProfile}
import play.api.libs.json.Json
import play.modules.reactivemongo.json._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}
import dao.MongoImplicits._



import scala.concurrent.ExecutionContext.Implicits.global
import reactivemongo.api.ReadPreference
import reactivemongo.api.commands.UpdateWriteResult

/**
  * Created by senthil
  */
class UserProfileDao  @Inject()(val reactiveMongoApi: ReactiveMongoApi) extends MongoController
  with ReactiveMongoComponents with GingerDao[UserProfile] {

  /*
  def userProfCollection: Future[JSONCollection] = database.map {
    _.collection[JSONCollection]("userProfile")
  }

  def updateUserProfile(userProfile: UserProfile):Try[Boolean] = {
    getResult(userProfCollection.flatMap( coll =>
      findById(userProfile._id, coll).map(o => processUserProfileUpdate(userProfile, o))))
  }

  private def processUserProfileUpdate(userProfile: UserProfile, profileFromDb: Option[UserProfile]):Try[Boolean] = {
    profileFromDb match {
      case Some(up) if (userProfile.userName.equals(up.userName) && userProfile._id.equals(up._id)) => saveUserProfile(userProfile)
      case Some(up) if (!userProfile.userName.equals(up.userName) || !userProfile._id.equals(up._id)) => Failure(new RuntimeException("User Name already exists"))
      case None => saveUserProfile(userProfile)
    }
  }

  private def saveUserProfile(userProfile: UserProfile):Try[Boolean] = {


    val eventualFuture: Future[UpdateWriteResult] = userProfCollection.flatMap(
      _.update(Json.obj("_id" -> userProfile._id), userProfile, upsert = true)
    )
    val result:UpdateWriteResult = getResult(eventualFuture)
    result
  }

  def findById(emailId: String, collection: JSONCollection):Future[Option[UserProfile]] = {

      val result  = collection.find(Json.obj("_id" -> emailId)).one[UserProfile]
      result
  }

  def findById(emailId: String):Future[Option[UserProfile]] = {

    userProfCollection.flatMap(findById(emailId, _ ))
  }
  */
}

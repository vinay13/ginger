package dao

import javax.inject.Inject

import model.JsonFormats._
import model.User
import org.mindrot.jbcrypt.BCrypt
import play.api.libs.json.Json
import play.modules.reactivemongo.json._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


/**
  * Created by senthil
  */
class UserDao @Inject()(val reactiveMongoApi: ReactiveMongoApi) extends MongoController
  with ReactiveMongoComponents with GingerDao[User] {/*

  def userCollection: Future[JSONCollection] = database.map {
    _.collection[JSONCollection]("user")
  }

  def getUser(email: String): Future[Option[User]] = {
    userCollection.flatMap(
      findByEmailId(email, _)
    )
  }

  def createUser(user: User) = {
    getResult(userCollection.map(
      _.update(Json.obj("email" -> user.emailId), user, upsert = true)
    ))
  }



  def exists(emailId: String) = {
    getResult(userCollection.map(
      _.find(Json.obj("emailId" -> emailId)).one[User]
    ))
  }

  def validateUserIdPassword(emailId: String, pass: String) = {
    val user = getResult(userCollection.flatMap(
      _.find(Json.obj("emailId" -> emailId)).one[User]
    ));



    user.map(u => BCrypt.checkpw(pass, u.password)).getOrElse(false);
  }

  def findByEmailId(emailId: String): Option[User] = {

    getResult(userCollection.map(coll => {
      val awaitable = coll.find(Json.obj("emailId" -> emailId)).one[User]
      getResult(awaitable)
    }
    ))
  }


  def findByEmailId(emailId: String, collection: JSONCollection) = {
    val awaitable = collection.find(Json.obj("emailId" -> emailId)).one[User]
    awaitable
  }*/


}

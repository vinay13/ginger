package model

import java.util.Date


import play.api.libs.json._
import reactivemongo.bson.Macros.Annotations.Key

/**
  * Created by senthil
  */
/*case class User(@Key("_id") _id: String, deviceId: String, emailId: String, password: String, partnerId: String, userSecurity: UserSecurity) {
  def updatePassword(password: String) = User(_id, deviceId, emailId, password, partnerId, userSecurity);
}

case class UserSecurity(secId: String, secKey: String){
}*/

/*case class UserProfile(@Key("_id") _id: String, userName: String, fullName: String, webLink: String){
  def withEmailId(emailId:String) = UserProfile(emailId, userName, fullName, webLink)
  def this(id: String) = this(id, null, null, null)
}*/



/*object UserProfile{

  /*def apply(_id: String , userName: String, fullName: String, webLink: String) = UserProfile(_id,userName,fullName,webLink)*/

 /* def apply(userName: String, fullName: String):UserProfile = UserProfile(null, userName, fullName, null)

  def apply(userName: String, fullName: String, webLink: String):UserProfile = UserProfile(null, userName, fullName, webLink)*/




}*/

/*
object UserProfileSerializers{
  import play.api.libs.functional.syntax._
  implicit val userProfileRequests: Reads[UserProfile] = (
    (JsPath \ "_id").read[String].orElse(Reads.pure(null)) and
      (JsPath \ "userName").read[String] and
      (JsPath \ "fullName").read[String] and
      (JsPath \ "webLink").read[String]
    ) (UserProfile.apply _)

  import play.api.libs.json.Json

  val userProfileFormat =  Json.format[UserProfile]



  /*implicit val userProfileFormatter: OFormat[UserProfile] = new OFormat[UserProfile] {
    override def writes(o: UserProfile): JsObject = userProfileFormat.writes(o)

    override def reads(json: JsValue): JsResult[UserProfile] = userProfileFormat.reads(json)
  }*/

  implicit val userProfileWrites: OWrites [UserProfile] = new OWrites[UserProfile] {
    override def writes(o: UserProfile): JsObject = userProfileFormat.writes(o)
  }

}
*/


//case class JwtToken(token:String,  expiry: Date)

case class SubjectClaim(emailId: String, partnerId: String, expiry: Date)
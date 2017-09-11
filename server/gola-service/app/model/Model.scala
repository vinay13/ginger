package model

import java.util.Date

import com.google.gson.Gson
import dto.{SignInRequest, SignUpRequest}
import play.api.libs.json._
import reactivemongo.bson.Macros.Annotations.Key

/**
  * Created by senthil
  */




//case class Tabs(id: String, category :Option(String), )


object JsonFormats {

  import play.api.libs.json.Json

  // Generates Writes and Reads for Feed and User thanks to Json Macros





  implicit val signUpFormat = Json.format[SignUpRequest]

  implicit val signInFormat = Json.format[SignInRequest]

  /*val userSecurityFormat = Json.format[UserSecurity]


  val gifMetaDataFormat = Json.format[GifMetaData]


  implicit val userSecurityFormatter: OFormat[UserSecurity] = new OFormat[UserSecurity] {
    override def writes(o: UserSecurity): JsObject = userSecurityFormat.writes(o)

    override def reads(json: JsValue): JsResult[UserSecurity] = userSecurityFormat.reads(json)
  }

  val userFormat = Json.format[User]
*/

  /*implicit val signUpFormatter: OFormat[SignUpRequest] = new OFormat[SignUpRequest] {
    override def writes(o: SignUpRequest): JsObject = signUpFormat.writes(o)

    override def reads(json: JsValue): JsResult[SignUpRequest] = signUpFormat.reads(json)
  }*/


  /*implicit val userFormatter: OFormat[User] = new OFormat[User] {
    override def writes(o: User): JsObject = userFormat.writes(o)

    override def reads(json: JsValue): JsResult[User] = userFormat.reads(json)
  }


  implicit val gifMetaDataFormatter: OFormat[GifMetaData] = new OFormat[GifMetaData] {
    override def writes(o: GifMetaData): JsObject = gifMetaDataFormat.writes(o)

    override def reads(json: JsValue): JsResult[GifMetaData] = gifMetaDataFormat.reads(json)
  }

  implicit val userWrites: OWrites[User] = new OWrites[User] {
    override def writes(o: User): JsObject = userFormat.writes(o)
  }*/





}
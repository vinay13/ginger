package dto

import model.User
import play.api.libs.json._

/**
  * Created by senthil
  */
class Dto {

}

case class Result(values:List[String])


case class UploadResponse(id: String, url: String)

case class Suggestion(tags: List[String], categories: List[String])



case class SignUpRequest(emailId: String,  partnerId: String, password: String)

case class SignInRequest(emailId: String,  password: String)

case class TagRequest(id: String,  tags: List[String])

case class CategoryRequest(id: String, categories: List[String])

case class GifMetaDataUpdateRequest(tags: List[String], categories: List[String])

object DtoSerializers {
  implicit  val tagRequestFormatter = Json.format[TagRequest]

  implicit  val uploadResponseFormatter = Json.format[UploadResponse]

  implicit  val categoryRequestFormatter = Json.format[CategoryRequest]

  val suggestionFormat = Json.format[Suggestion]

  implicit  val resultFormat = Json.format[Result]

  implicit val suggestionFormatter: OFormat[Suggestion] = new OFormat[Suggestion] {
    override def writes(o: Suggestion): JsObject = suggestionFormat.writes(o)

    override def reads(json: JsValue): JsResult[Suggestion] = suggestionFormat.reads(json)
  }
}

object GifMetaDataUpdateRequest{
  val gifMetaDataUpdateRequestFormat = Json.format[GifMetaDataUpdateRequest]


  /*implicit val signUpFormatter: OFormat[SignUpRequest] = new OFormat[SignUpRequest] {
    override def writes(o: SignUpRequest): JsObject = signUpFormat.writes(o)

    override def reads(json: JsValue): JsResult[SignUpRequest] = signUpFormat.reads(json)
  }*/


  implicit val gifMetaDataUpdateRequestFormatter: OFormat[GifMetaDataUpdateRequest] = new OFormat[GifMetaDataUpdateRequest] {
    override def writes(o: GifMetaDataUpdateRequest): JsObject = gifMetaDataUpdateRequestFormat.writes(o)

    override def reads(json: JsValue): JsResult[GifMetaDataUpdateRequest] = gifMetaDataUpdateRequestFormat.reads(json)
  }

}

package controllers

import java.util.Date
import javax.inject.Inject

import akka.stream.Materializer
import com.google.gson.Gson
import dao.JWT
import dto.SignInResponse
import model.{UserProfile, UserSecurity, UserSource}
import org.slf4j.LoggerFactory.getLogger
import play.api.Configuration
import play.api.libs.json.Json
import play.api.libs.oauth.{ConsumerKey, OAuth, ServiceInfo}
import play.api.libs.ws.{WS, WSClient, WSResponse}
import play.api.mvc.{Action, Controller, Result}
import springconfig.context.SpringAppContext
import utils.GsonConfig

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by senthil
  */
class GoogleAuthController @Inject()(config: Configuration, implicit val mat: Materializer, wsClient: WSClient) extends Controller {

  val userService = SpringAppContext.getUserService

  private val logger = getLogger(classOf[GoogleAuthController])

  val gson = GsonConfig.newGson()

  private val gConfig = config.getConfig("google").get
  val clientId = gConfig.getString("clientId").get
  val clientSecret = gConfig.getString("clientSecret").get
  val redirectUrl = gConfig.getString("redirectUrl").get

  def strip(quoted: String): String = {
    quoted.filter(char => char != '\"')
  }

  def oauth2callback(state: String, code: String) = Action.async {
    logger.info("++++++++++++++++++++++++++++++++++++++")
    logger.info(s"Received code ${code}, state ${state}")
    val postBody = "code=" + code + "&client_id=" + clientId +
      "&client_secret=" + clientSecret +
      "&redirect_uri=" + redirectUrl + "&grant_type=authorization_code"
    val response = wsClient.url("https://accounts.google.com/o/oauth2/token").withHeaders("Content-Type" -> "application/x-www-form-urlencoded").post(postBody)
    getStatusFromAccessResponse(response)
  }


  private def getStatusFromAccessResponse(response: Future[WSResponse]): Future[Result] = {
    val result = response.map(r => {
      if(r.status == 200 || r.status == 201) {
        val json = r.json
        logger.info("access token Response  {}", json)
        val accessToken = strip((json \ "access_token").get.toString())
        val url = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + accessToken
        logger.info("user info url is {}", url)
        val userInfoResponse = wsClient.url(url).get
        getStatusFromUserResponse(userInfoResponse)
      }else{
        logger.warn("access token Response  {}", r.json)
        Status(r.status)
      }
    })
    result
  }


  private def getStatusFromUserResponse(userInfoResponse: Future[WSResponse]): Result = {
    Await.result(
      userInfoResponse.map( { userResponse =>
        if(userResponse.status == 200 || userResponse.status == 201) {
          val userJson = userResponse.json
          logger.info("user Response  {}", userResponse)
          val email = strip((userJson \ "email").toString())
          val fullName = strip((userJson \ "name").toString())
          val token = getOrCreateUser(email, fullName)
          val json = gson.toJson(token)
          Ok(json)
        }else{
          logger.warn("user Response  {}", userResponse.json)
          Status(userResponse.status)
        }
      }), Duration.Inf)
  }

  def getOrCreateUser(emailId:String, name: String):SignInResponse={
    userService.findUserByEmailId(emailId) match {
      case Some(user) => {
        val token = JWT.generateJWT(user.getEmailId, user.getPartnerId, new Date(System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000)), user.getUserSecurity)
        new SignInResponse(token)
      }
      case None => {
        val token = userService.createUserAndGenerateJWTToken(emailId, "ginger", emailId.reverse + "123", UserSource.FaceBook)
        val userProfile = new UserProfile()
        userProfile.setId(emailId)
        userProfile.setFullName(name)
        userService.updateUserProfile(userProfile)
        token
      }
    }
  }
}

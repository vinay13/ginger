package controllers

import java.util.UUID
import javax.inject.Inject

import akka.stream.Materializer
import com.google.gson.Gson
import com.restfb.types.User
import com.restfb.{DefaultFacebookClient, Parameter, Version}
import model.UserSource
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory.getLogger
import play.api.Configuration
import play.api.libs.ws.{WSClient, WSResponse}
import play.api.mvc.{Action, Controller, Result}
import springconfig.context.SpringAppContext
import utils.GsonConfig

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.matching.Regex

/**
  * Created by senthil
  */
class FaceBookAuthController @Inject()(config: Configuration, implicit val mat: Materializer, wsClient: WSClient) extends Controller {

  private val fConfig = config.getConfig("facebook").get
  val clientId = fConfig.getString("clientId").get
  val clientSecret = fConfig.getString("clientSecret").get
  val redirectUrl = fConfig.getString("redirectUrl").get

  val userService = SpringAppContext.getUserService

  private val logger = getLogger(classOf[FaceBookAuthController])

  def oauth2callback(code: String) = Action.async {
    /*logger.info(s"code received is ${code}")
    if (StringUtils.isNotBlank(code)) {
      val accessTokenUrl = "https://graph.facebook.com/oauth/access_token?client_id=" + clientId + "&client_secret=" + clientSecret + "&code=" + code + "&redirect_uri=" + redirectUrl
      val accessTokenResponse = wsClient.url(accessTokenUrl).get()
      processAccessTokenResponse(accessTokenResponse)
    } else {
      Future.successful(Unauthorized)
    }*/

    val facebookClient = new DefaultFacebookClient(code, Version.VERSION_2_9)
    val fbUser = facebookClient.fetchObject("me", classOf[User], Parameter.`with`("fields", "email"))
    logger.info("fbUser email is " + fbUser.getEmail)
    logger.info("fbUser is " + fbUser)
    val token = userService.createUserAndGenerateJWTToken(fbUser.getEmail, "ginger", UUID.randomUUID().toString, UserSource.FaceBook)
    Future.successful(Ok(gson.toJson(token)))
  }

  val gson = GsonConfig.newGson()


  private def processAccessTokenResponse(accessTokenResponse: Future[WSResponse]): Future[Result] = {
    accessTokenResponse.map(response => {
      val body = response.body
      logger.info(s"access token response is ${body}")
      val regex = new Regex("access_token=(.*)&expires=(.*)")
      body match {
        case regex(accessToken, expires) => {
          val facebookClient = new DefaultFacebookClient(accessToken, Version.VERSION_2_9)
          val fbUser = facebookClient.fetchObject("me", classOf[User])
          val token = userService.createUserAndGenerateJWTToken(fbUser.getEmail, "ginger", UUID.randomUUID().toString, UserSource.FaceBook)
          Ok(gson.toJson(token))
        }
        case _ => {
          Ok("no match")
        }
      }
    })
  }
}

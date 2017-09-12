package controllers

import java.security.spec.KeySpec
import java.util.Base64.getEncoder
import java.util.{Base64, Date}
import javax.crypto.{Cipher, SecretKeyFactory}
import javax.inject.Inject

import com.google.gson.Gson
import dao.{GifMetaDataDao, JWT, UserDao, UserProfileDao}
import dto.{SignInRequest, SignInResponse, SignUpRequest, UnAuthorizedResponse}
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import model.JsonFormats._
import model._
import org.mindrot.jbcrypt.BCrypt
import org.slf4j.LoggerFactory.getLogger
import play.api.Configuration
import reactivemongo.api.commands.{LastError, UpdateWriteResult, WriteResult}
import reactivemongo.bson.BSONObjectID

import scala.concurrent.Future
import predef.Predef._
import spring.service.{MailService, UserService}
import springconfig.context.SpringAppContext
import utils.GsonConfig

import scala.io.Source
import scala.util.{Failure, Success}


/**
  * Created by senthil
  */
class SignUpController @Inject()(config: Configuration) extends Controller with GingerActionBuilders {

  val gson = GsonConfig.newGson()

  override def userFinder: UserService = SpringAppContext.getUserService

  val mailConfig = config.getConfig("ginger.mail").get.underlying

  val gingerBaseUrl = config.getConfig("ginger").get.underlying.getString("webBaseUrl")

  val activationTemplatePath = config.getConfig("ginger").get.underlying.getString("activationMailTemplate")

  val requireSignUpActivation = config.getConfig("ginger").get.underlying.getBoolean("requireSignUpActivation")

  val activationSubjectLine = config.getConfig("ginger").get.underlying.getString("accountActivationSubjectLine")

  val mailService = new MailService(mailConfig.getString("host"),
    mailConfig.getInt("port"),
    mailConfig.getString("fromEmail"),
    mailConfig.getString("pass")
  )

  private val logger = getLogger(classOf[SignUpController])

  def getUser(emailId: String): Action[AnyContent] = AuthenticatedAction {
    val userByEmailId: Option[User] = userFinder.findUserByEmailId(emailId)
    val maybeResult: Option[Result] = userByEmailId.map(u => {
      Ok(gson.toJson(u))
    })
    maybeResult.getOrElse(NotFound)
  }


  def getUserProfile() = AuthenticatedAction {

    implicit request =>
      val emailId = request.headers.get("currentUser").get
      val userProfileOption = userFinder.findProfileByEmailId(emailId)
      Ok(gson.toJson(userProfileOption.getOrElse(new UserProfile(emailId))))
  }

  def updateUserProfile() = AuthenticatedAction {
    implicit request =>
      val emailId = request.headers.get("currentUser").get
      val userProfile = gson.fromJson(request.body.asJson.get.toString(), classOf[UserProfile])
      logger.info("User Profile is {}", userProfile)
      userFinder.updateUserProfile(userProfile)
      Ok
  }

  def getUserAuthStatus() = AuthenticatedAction {
    Ok(gson.toJson(new UnAuthorizedResponse(false, false)))
  }



  def signUp(): Action[AnyContent] = Action.async {

    request: Request[AnyContent] => {

      Future {
        import model.JsonFormats._

        val signUpRequest = Json.fromJson[SignUpRequest](request.body.asJson.get).get

        val user: Option[User] = userFinder.findUserByEmailId(signUpRequest.emailId)
        user match {
          case Some(u) => Status(409)
          case None =>
            if(requireSignUpActivation) {
              val generateToken = userFinder.createUserAndGenerateToken(signUpRequest.emailId, signUpRequest.partnerId, signUpRequest.password, false, UserSource.Native)
              val template = Source.fromInputStream(classOf[SignUpController].getClassLoader.getResourceAsStream(activationTemplatePath)).mkString
              mailService.sendActivationEmail(generateToken.getToken, signUpRequest.emailId, gingerBaseUrl, template, activationSubjectLine, signUpRequest.password)
              val user: User = userFinder.findUserByEmailId(signUpRequest.emailId).get
              val signInResponse = new SignInResponse(JWT.generateJWT(signUpRequest.emailId, user.getPartnerId, new Date(System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000)), user.getUserSecurity))
              Ok (gson.toJson(signInResponse))
            }else{
              val generateToken = userFinder.createUserAndGenerateJWTToken(signUpRequest.emailId, signUpRequest.partnerId, signUpRequest.password, UserSource.Native)
              //mailService.sendActivationEmail(generateToken.getToken, signUpRequest.emailId, gingerBaseUrl)
              Ok(gson.toJson(generateToken))
            }

        }
      }
    }
  }

  def signIn(): Action[AnyContent] = Action.async {

    request: Request[AnyContent] => {

      import model.JsonFormats._

      val signInRequest = Json.fromJson[SignInRequest](request.body.asJson.get).get

      val validUser = userFinder.validateUserIdPassword(signInRequest.emailId, signInRequest.password);
      if (!validUser)
        Future.successful(Unauthorized)
      else {
        val user: User = userFinder.findUserByEmailId(signInRequest.emailId).get
        val token = JWT.generateJWT(user.getEmailId, user.getPartnerId, new Date(System.currentTimeMillis() + (1000 * 24 * 60 * 60 * 1000)), user.getUserSecurity)
        Future.successful(Ok(gson.toJson(new SignInResponse(token))))
      }
    }
  }

  def signInForWeb(): Action[AnyContent] = Action.async {

    request: Request[AnyContent] => {

      import model.JsonFormats._

      val signInRequest = Json.fromJson[SignInRequest](request.body.asJson.get).get

      val validUser = userFinder.validateUserIdPassword(signInRequest.emailId, signInRequest.password);
      if (!validUser)
        Future.successful(Unauthorized)
      else {
        val user: User = userFinder.findUserByEmailId(signInRequest.emailId).get
        val token = JWT.generateJWT(user.getEmailId, user.getPartnerId, new Date(System.currentTimeMillis() + (60 * 60 *  60 * 1000)), user.getUserSecurity)
        Future.successful(Ok(gson.toJson(new SignInResponse(token, user.getUserRoles))))
      }
    }
  }






  private def createUser(user: User) {
    //val hashpw = BCrypt.hashpw(user.getPassword, BCrypt.gensalt())
    //logger.info("convering user to json " + user.getPassword+ "," + hashpw)
    //logger.info("" + BCrypt.checkpw(user.getPassword, hashpw));
    //logger.info("111111++++" + BCrypt.checkpw(user.getPassword, hashpw));
    //val modifiedUser = user.setPassword(hashpw)
    userFinder.createUser(user)

  }


  private def createUserObject(emailId: String, partnerId: String, password: String, userSecurity: UserSecurity) = {
    val hashpw = BCrypt.hashpw(password, BCrypt.gensalt())
    logger.info("1111++++" + BCrypt.checkpw(password, hashpw));
    logger.info("hashpws is " + hashpw);
    new User("testdeviceid replace it", emailId, hashpw, partnerId, userSecurity)
  }

  def activateUser(emailId:String, token: String) =  Action {
    val decoder = Base64.getDecoder
    val decodedEmailId = new String(decoder.decode(emailId.getBytes()))
    val decodedToken = new String(decoder.decode(token.getBytes()))
    logger.info(s"Activation request for user ${emailId}, token ${token}")
    userFinder.activateUser(decodedEmailId, decodedToken)
    Ok("Activated")
  }

  def saveUserDeviceDetails()=AuthenticatedAction{
    implicit request =>
      val emailId = request.headers.get("currentUser").get
      val deviceRequest = gson.fromJson(request.body.asJson.get.toString(), classOf[Device])
        userFinder.updateUserDeviceDetails(emailId,deviceRequest)
      Ok
  }
}



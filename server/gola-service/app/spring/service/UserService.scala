package spring.service


import java.util.Base64.getEncoder
import java.util.{Date, UUID}

import dao.JWT
import dto.SignInResponse
import model._
import org.mindrot.jbcrypt.BCrypt
import org.slf4j.LoggerFactory.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import play.api.libs.json.Json
import play.api.mvc.Result
import springconfig.repo.{ActivationTokenRepository, UserProfileRepository, UserRepository}

import scala.util.Failure

/**
  * Created by senthil
  */

@Service
class UserService(@Autowired userRepository: UserRepository,
                  @Autowired userProfileRepository: UserProfileRepository,
                  @Autowired activationTokenRepository: ActivationTokenRepository) {
  
  def createActivationToken(email: String) = {
    val activationToken = new ActivationToken()
    activationToken.setEmailId(email)
    activationToken.setCreatedDate(new Date())
    activationToken.setToken(UUID.randomUUID().toString)
    activationTokenRepository.save(activationToken)
    activationToken
  }

  def validateToken(email: String, token: String) = {
    val tokens = activationTokenRepository.findByEmailId(email, new Sort(Sort.Direction.DESC, "createdDate"))
    if (!tokens.isEmpty) {
      val token = tokens.get(0)
      activationTokenRepository.delete(token)
      token.getEmailId.equals(email) && token.getToken.equals(token)
    } else {
      false
    }
  }

  def validateUserIdPassword(emailId: String, pass: String): Boolean = {
    val user = userRepository.findByEmailId(emailId)
    if (!user.isActive) false
    else {
      if (user == null) false;
      else {
       // println("user pass is " + pass + ",," + user.getPassword + ", " + BCrypt.checkpw(pass, user.getPassword) + "," + user.getEmailId)

        //logger.info(s"user pass is ${pass} ,,  ${user.getPassword}  ,  ${BCrypt.checkpw(pass, user.getPassword)}  ,  ${user.getEmailId} ")
        BCrypt.checkpw(pass, user.getPassword)
      }
    }
  }

  def findUserByEmailId(emailId: String): Option[User] = {
    val result = userRepository.findByEmailId(emailId)
    if (result == null)
      None
    else
      Some(result)
  }

  private val logger = getLogger(classOf[UserService])

  def findProfileByEmailId(emailId: String): Option[UserProfile] = {
    val result = userProfileRepository.findOne(emailId)
    logger.info(s"Getting profile by emailId ${emailId} profile ${result} ")
    if (result == null)
      None
    else
      Some(result)
  }

  def updateUserProfile(userProfile: UserProfile) = {
    val userProfileFromDb = userProfileRepository.findOne(userProfile.getId())
    if (userProfileFromDb != null &&
      (userNamePresent(userProfileFromDb) && userNamePresent(userProfile) && !userProfileFromDb.getUserName.equalsIgnoreCase(userProfile.getUserName))) {
      throw new RuntimeException("User Name has changed")
    } else {
      userProfileRepository.save(userProfile)
    }
  }

  def createUser(user: User): Unit = {
    userRepository.save(user)
  }

  def sendActivationMail(host: String, port: Int, email: String, fromEmail: String, pass: String): Unit = {

  }

  private def userNamePresent(userProfileFromDb: UserProfile) = {
    userProfileFromDb.getUserName != null
  }

  def findByAccessKey(accessKey: String) = {
    userRepository.findByUserSecuritySecId(accessKey)
  }

  def activateUser(emailId: String,  token: String) = {
    val tokenList = activationTokenRepository.findByEmailId(emailId, new Sort(Sort.Direction.DESC, "createdDate"))
    if(!tokenList.isEmpty){
      val activationToken = tokenList.get(0)
      if(activationToken.getToken.equals(token)){
        val user = userRepository.findByEmailId(emailId)
        user.setActive(true)
        userRepository.save(user)
        activationTokenRepository.delete(tokenList)
      }
    }
  }

  def createUserAndGenerateToken(emailId: String, partner: String, password: String, activate: Boolean, userSource: UserSource): ActivationToken = {
    val security: UserSecurity = createNormalUser(emailId, partner, password, activate, userSource)
    createActivationToken(emailId)
  }

  def createUserAndGenerateJWTToken(emailId: String, partner: String, password: String, userSource: UserSource): SignInResponse = {
    val security: UserSecurity = createNormalUser(emailId, partner, password, true, userSource)
    new SignInResponse(JWT.generateJWT(emailId, partner, new Date(System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000)), security))
  }

  private def createNormalUser(emailId: String, partner: String, password: String, activate: Boolean,  userSource: UserSource) = {
    logger.info("token and password hash ")
    val security: UserSecurity = generateUserSecurityDetails(emailId)
    val user = createUserObject(emailId, partner, password, security)
    user.setUserSource(userSource)
    user.getUserRoles.add(UserRole.Normal)
    user.setActive(activate)
    createUser(user)
    security
  }

  private def createUserObject(emailId: String, partnerId: String, password: String, userSecurity: UserSecurity) = {
    val hashpw = BCrypt.hashpw(password, BCrypt.gensalt())
    logger.info("1111++++" + BCrypt.checkpw(password, hashpw));
    logger.info("hashpws is " + hashpw);
    new User("testdeviceid replace it", emailId, hashpw, partnerId, userSecurity)
  }

  private def generateUserSecurityDetails(emailId: String) = {
    val keys = JWT.generateUserKeys(emailId)
    val encryptedBytes = JWT.encrypt(keys._2.getBytes(), keys._1)
    val base64Encoded = getEncoder().encode(encryptedBytes)
    val usec = new String(base64Encoded)
    val security = new UserSecurity(keys._1, usec)
    security
  }

  /*def getOrCreateUser(emailId: String, name: String): Token = {
    findUserByEmailId(emailId) match {
      case Some(user) => {
        val token = JWT.generateJWT(user.getEmailId, user.getPartnerId, new Date(System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000)), user.getUserSecurity)
        Token(token)
      }
      case None => {
        val token = createUserAndGenerateToken(emailId, "ginger", emailId.reverse + "123")
        val userProfile = new UserProfile()
        userProfile.setId(emailId)
        userProfile.setFullName(name)
        updateUserProfile(userProfile)
        token
      }
    }
  }*/

  def getContentCreatorsUserId() = {
    import scala.collection.JavaConverters._
    userRepository.findByUserRoles(UserRole.ContentCreator).asScala.map(_.getEmailId).toList.asJavaCollection
  }

  def getTaggersUserId() = {
    import scala.collection.JavaConverters._
    userRepository.findByUserRoles(UserRole.Tagger).asScala.map(_.getEmailId).toList.asJavaCollection
  }

  def updateUserDeviceDetails(emailId: String, deviceRequest: Device) = {
    var user=findUserByEmailId(emailId);

   user.map(user=>{
     user.setDevice(deviceRequest);
     userRepository.save(user)
   })
    
  }




}

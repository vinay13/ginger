package controllers

import com.google.gson.Gson
import dao.{JWT, UserDao}
import dto.UnAuthorizedResponse
import model.UserRole
import org.slf4j.{Logger, LoggerFactory}
import play.api.http.Status.UNAUTHORIZED
import play.api.mvc.{Action, ActionBuilder, Request, Result}
import play.api.mvc._
import play.core.parsers._
import spring.service.UserService
import springconfig.context.SpringAppContext

import scala.concurrent.Future

/**
  * Created by senthil
  */

trait GingerActionBuilders {

  def userFinder: UserService

  val authLogger: Logger = LoggerFactory.getLogger(this.getClass)

  def ConsumerAction = new ActionBuilder[Request] {
    override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]): Future[Result] = {
      authLogger.info(s"consumer action invoked--------------------")
      request.headers.get("X-Gola-Access-Key") match {
        case Some(header: String) => getConsumerResult(request, block, header)
        case None => {
          request.queryString.get("X-Gola-Access-Key") match{
            case Some(accessKey: Seq[String]) => getConsumerResult(request, block, accessKey.head)
            case None => Future.successful(Results.Unauthorized)
          }
        }
      }
    }
  }

  private def getConsumerResult[A](request: Request[A], block: (Request[A]) => Future[Result], header: String): Future[Result] = {

    val user = userFinder.findByAccessKey(header)
    if(user == null)
      Future.successful(Results.Unauthorized)
    else{
      val updateRequest = new WrappedRequest(request) {
        override def headers: Headers = request.headers.add(("currentUser", user.getEmailId))
      }
      authLogger.info(s"Consumer authentication succesful ${user.getEmailId}")
      block(updateRequest)
    }
  }

  def AuthenticatedAction = new ActionBuilder[Request] {
    override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]): Future[Result] = {
      authLogger.info(s"auth action invoked--------------------")
      request.headers.get("authorization") match {
        case Some(header: String) if (header.startsWith("Bearer ")) => {
          authLogger.info(s"Token found ${header}")
          getResult(request, block, header)
        }
        case Some(header: String) if (!header.startsWith("Bearer ")) => Future.successful(Results.Unauthorized)
        case None => Future.successful(Results.Unauthorized)
      }
    }
  }

  def AdminAction = new ActionBuilder[Request] {
    override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]): Future[Result] = {
      authLogger.info(s"admin action invoked------------")
      request.headers.get("authorization") match {
        case Some(header: String) if (header.startsWith("Bearer ")) => getResult(request, block, header, UserRole.Admin)
        case Some(header: String) if (!header.startsWith("Bearer ")) => Future.successful(Results.Unauthorized)
        case None => Future.successful(Results.Unauthorized)
      }
    }
  }

  def AuthAction(role: List[UserRole]) = new ActionBuilder[Request] {
    override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]): Future[Result] = {
      authLogger.info(s"admin action invoked ${role}-------------")
      request.headers.get("authorization") match {
        case Some(header: String) if (header.startsWith("Bearer ")) => getResult(request, block, header, role)
        case Some(header: String) if (!header.startsWith("Bearer ")) => Future.successful(Results.Unauthorized)
        case None => Future.successful(Results.Unauthorized)
      }
    }
  }

  def OptionalAuthenticatedAction = new ActionBuilder[Request] {
    override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]): Future[Result] = {
      request.headers.get("authorization") match {
        case Some(header: String) if (header.startsWith("Bearer ")) => getResult(request, block, header)
        case Some(header: String) if (!header.startsWith("Bearer ")) => block(request)
        case None => block(request)
      }
    }
  }

  private def getResult[A](request: Request[A], block: (Request[A]) => Future[Result], header: String, userRole: UserRole = UserRole.Normal): Future[Result] = {

    val claim = JWT.getSubject(header.replace("Bearer ", ""))
    authLogger.info(s"JWT Claim is ${claim}")
    userFinder.findUserByEmailId(claim.emailId) match {
      case Some(user) => {
        if (user.getUserRoles.contains(userRole) && user.isActive) {
          val validToken = JWT.verifyToken(header.replace("Bearer ", ""), claim, user.getUserSecurity)
          if (validToken) {
            val updateRequest = new WrappedRequest(request) {
              override def headers: Headers = request.headers.add(("currentUser", user.getEmailId))
            }
            block(updateRequest)
          } else
            Future.successful(Results.Unauthorized)
        }else if(!user.isActive)
          Future.successful(Results.Unauthorized(new Gson().toJson(new UnAuthorizedResponse(true, false))))
        else
          Future.successful(Results.Unauthorized)
      }
      case None => Future.successful(Results.Unauthorized)
    }
  }

  private def getResult[A](request: Request[A], block: (Request[A]) => Future[Result], header: String, userRoles: List[UserRole] ): Future[Result] = {
    import scala.collection.JavaConverters._
    val claim = JWT.getSubject(header.replace("Bearer ", ""))
    userFinder.findUserByEmailId(claim.emailId) match {
      case Some(user) => {
        val javaList = userRoles.asJava
        if (user.getUserRoles.asScala.filter(javaList.contains(_)).size > 0 && user.isActive) {
          val validToken = JWT.verifyToken(header.replace("Bearer ", ""), claim, user.getUserSecurity)
          if (validToken) {
            val updateRequest = new WrappedRequest(request) {
              override def headers: Headers = request.headers.add(("currentUser", user.getEmailId))
            }
            block(updateRequest)
          } else
            Future.successful(Results.Unauthorized)
        }else
          Future.successful(Results.Unauthorized)
      }
      case None => Future.successful(Results.Unauthorized)
    }
  }
}

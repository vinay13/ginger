package springconfig.repo

import model.{User, UserRole}
import org.springframework.data.mongodb.repository.MongoRepository

/**
  * Created by senthil
  */
trait UserRepository extends  MongoRepository[User, String]{

  def findByEmailId(emailId:String):User

  def findByUserRoles(userRole: UserRole):java.util.List[User]

  def findByUserSecuritySecId(secId: String): User

}

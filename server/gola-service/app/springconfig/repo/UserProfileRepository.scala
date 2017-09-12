package springconfig.repo

import model.{User, UserProfile}
import org.springframework.data.mongodb.repository.MongoRepository

/**
  * Created by senthil
  */
trait UserProfileRepository extends  MongoRepository[UserProfile, String]{

  def findById(emailId:String):UserProfile

}

package springconfig.repo

import model.ActivationToken
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.repository.MongoRepository

/**
  * Created by senthil
  */
trait ActivationTokenRepository extends MongoRepository[ActivationToken, String]{

  def findByEmailId(emailId: String, sort: Sort):java.util.List[ActivationToken]

}

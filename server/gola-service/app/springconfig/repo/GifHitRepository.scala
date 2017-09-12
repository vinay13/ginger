package springconfig.repo


import model.{GifHit}
import org.springframework.data.mongodb.repository.MongoRepository

/**
  * Created by senthil
  */

trait GifHitRepository  extends  MongoRepository[GifHit, String]{

  def findByEmailId(emailId: String):java.util.List[GifHit]
}

package springconfig.repo

import model.SearchHit
import org.springframework.data.mongodb.repository.MongoRepository

/**
  * Created by senthil
  */
trait SearchHitRepository extends MongoRepository[SearchHit, String]{

  def findByTextOrderByTimestampDesc(text:String):SearchHit

}

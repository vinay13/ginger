package springconfig.repo

import model.LowResGifGenerationFailure
import org.springframework.data.mongodb.repository.MongoRepository

/**
  * Created by senthil
  */
trait LowResGifGenerationRepository extends MongoRepository[LowResGifGenerationFailure, String]{

  def findByGifId(gifId: String):LowResGifGenerationFailure
}

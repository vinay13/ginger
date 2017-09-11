package springconfig.repo

import model.TrendingGif
import org.springframework.data.mongodb.repository.MongoRepository

/**
  * Created by senthil
  */
trait TrendingGifRepository extends MongoRepository[TrendingGif, String]{

}

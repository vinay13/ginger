package springconfig.repo

import model.{AllTags, CuratedTab}
import org.springframework.data.mongodb.repository.MongoRepository

/**
  * Created by senthil
  */
trait AllTagsRepository extends  MongoRepository[AllTags, String]{



}

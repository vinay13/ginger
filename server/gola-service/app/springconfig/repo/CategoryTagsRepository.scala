package springconfig.repo

import model.{ActivationToken, CategoryTags}
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.repository.MongoRepository

/**
  * Created by senthil
  */
trait CategoryTagsRepository extends MongoRepository[CategoryTags, String]{
}

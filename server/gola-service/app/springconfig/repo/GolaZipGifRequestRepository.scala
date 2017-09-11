package springconfig.repo

import model.{GolaZipGifRequest, TagUpdateAudit}
import org.springframework.data.mongodb.repository.MongoRepository

trait GolaZipGifRequestRepository  extends MongoRepository[GolaZipGifRequest, String]{

}

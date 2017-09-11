package springconfig.repo

import model.BulkUploadRequest
import org.springframework.data.mongodb.repository.MongoRepository

/**
  * Created by senthil
  */
trait BulkUploadRequestRepository extends MongoRepository[BulkUploadRequest, String]{
}

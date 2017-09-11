package springconfig.repo

import model.RequestedFile
import org.springframework.data.mongodb.repository.MongoRepository

/**
  * Created by senthil
  */
trait RequestedFileRepository extends MongoRepository[RequestedFile, String]{
}

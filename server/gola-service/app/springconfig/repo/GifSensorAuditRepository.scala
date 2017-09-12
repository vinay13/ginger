package springconfig.repo

import model.GifCensorAudit
import org.springframework.data.mongodb.repository.MongoRepository

/**
  * Created by senthil
  */
trait GifSensorAuditRepository extends MongoRepository[GifCensorAudit, String] {
}

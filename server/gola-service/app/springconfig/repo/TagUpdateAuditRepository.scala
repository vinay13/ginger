package springconfig.repo

import model.{ TagUpdateAudit, TrendingGif}
import org.springframework.data.domain.{Page, Pageable}
import org.springframework.data.mongodb.repository.MongoRepository

/**
  * Created by senthil
  */
trait TagUpdateAuditRepository extends MongoRepository[TagUpdateAudit, String]{

  def findByTaggerId(userId: String):java.util.List[TagUpdateAudit]

  def findByGifIdIn(gifIdList: java.util.List[String]):java.util.List[TagUpdateAudit]


  def findByTaggerId(emailId: String, pageable: Pageable): Page[TagUpdateAudit]

}

package springconfig.repo

import model.{GifShare, SearchHit}
import org.springframework.data.mongodb.repository.MongoRepository

/**
  * Created by senthil
  */
trait GifShareRepository extends MongoRepository[GifShare, String] {

  def findByTagsOrderByTimestampDesc(text:String):SearchHit

}

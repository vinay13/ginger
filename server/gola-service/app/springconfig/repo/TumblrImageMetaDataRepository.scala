package springconfig.repo

import java.util.regex.Pattern

import model.TumblrImageMetaData
import org.springframework.data.mongodb.repository.{MongoRepository, Query}

/**
  * Created by senthil
  */
trait TumblrImageMetaDataRepository extends MongoRepository[TumblrImageMetaData, String] {

  @Query("{\"photoUrl\":?0}")
  def findByPhotoUrlContaining(patter: Pattern):TumblrImageMetaData
}

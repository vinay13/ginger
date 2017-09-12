package springconfig.repo

import model.{FavouriteGif, GifHit}
import org.springframework.data.mongodb.repository.MongoRepository

/**
  * Created by senthil
  */
trait FaviourteGifRepository  extends  MongoRepository[FavouriteGif, String]{

  def findByEmailId(emailId:String): FavouriteGif
}

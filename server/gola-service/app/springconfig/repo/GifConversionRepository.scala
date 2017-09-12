package springconfig.repo

import dto.GifConversionStatus
import model.{GifConversion}
import org.springframework.data.mongodb.repository.MongoRepository

/**
  * Created by venkat
  */
trait GifConversionRepository extends MongoRepository[GifConversion,String]{

  def findByGifId(gifId: String):GifConversion

  def findByLowResGifConversionStatus(lowResGifConversionStatus: GifConversionStatus): java.util.List[GifConversion]

  def findByLowResWebpGifConversionStatus(lowResGifConversionStatus: GifConversionStatus): java.util.List[GifConversion]

  def findByWaterMarkedGifConversionStatus(waterMarkedGifConversionStatus: GifConversionStatus): java.util.List[GifConversion]

}

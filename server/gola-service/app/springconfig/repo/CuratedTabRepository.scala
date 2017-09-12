package springconfig.repo

import model.CuratedTab
import org.springframework.data.mongodb.repository.{MongoRepository, Query}

/**
  * Created by senthil
  */

trait CuratedTabRepository extends  MongoRepository[CuratedTab, String]{

  def  findByTopItemsGifId(gifId: String):java.util.List[CuratedTab];

  @Query("{\"id\":?0,  \"topItems.active\": true}, {id: 0, topItems: {$slice1: [?1, ?2]}}")
  def findByTabsId(tabId: String, from: Int, size: Int):CuratedTab

}

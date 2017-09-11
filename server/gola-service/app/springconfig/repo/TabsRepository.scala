package springconfig.repo

import model.{Idiom, Tabs}
import org.springframework.data.mongodb.repository.MongoRepository

/**
  * Created by senthil
  */
trait TabsRepository extends MongoRepository[Tabs, String]{

  def findByIdiom(idiom: Idiom): Tabs

  def findByTabsId(tabId: String): Tabs
}

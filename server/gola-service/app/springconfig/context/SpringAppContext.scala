package springconfig.context

import org.springframework.context.annotation.AnnotationConfigApplicationContext
import spring.service.{BulkUploadService, GifService, TabService, UserService}

/**
  * Created by senthil
  */
object SpringAppContext {

    val applicationContext = new AnnotationConfigApplicationContext("spring")

    def getUserService: UserService = applicationContext.getBean(classOf[UserService])

    def getGifService: GifService = applicationContext.getBean(classOf[GifService])

    def getBulkUploadService: BulkUploadService = applicationContext.getBean(classOf[BulkUploadService])

    def getTabService: TabService = applicationContext.getBean(classOf[TabService])

    /*def getJobService: JobService = applicationContext.getBean(classOf[JobService])

    def getPartnerLookupService: PartnerLookupService = applicationContext.getBean(classOf[PartnerLookupService])

    def getCampaignService = applicationContext.getBean(classOf[CampaignService])*/

}

package utils


import dto.{GifMetaDataDto, TopItem, TopItemDto}
import model._
import springconfig.repo.GifMetaDataRepository

object GifMetaDataConverter {

  def convertToDto(metaData: GifMetaData): GifMetaDataDto = {

    val baseUrl = metaData.getBaseUrl
    val originalFN = metaData.getOriginalFN

    val waterMarkedFN = if (metaData.getWaterMarkedFN != null) metaData.getWaterMarkedFN else originalFN
    val thumbNailFN = if (metaData.getThumbNailFN != null) metaData.getThumbNailFN else originalFN
    val lowResFN = if (metaData.getLowResFN != null) metaData.getLowResFN else originalFN
    val lowResWebpFN = if (metaData.getLowResWebpFN != null) metaData.getLowResWebpFN else originalFN
    val dto = new GifMetaDataDto(metaData.getId,
      baseUrl,
      waterMarkedFN,
      lowResFN,
      lowResWebpFN,
      thumbNailFN)
    dto.setWidth(metaData.getWidth)
    dto.setSize(metaData.getSize)
    dto.setHeight(metaData.getHeight)
    val lowResWidth = if (metaData.getLowResWidth == 0) metaData.getWidth else metaData.getLowResWidth
    val lowResHeight = if (metaData.getLowResHeight == 0) metaData.getHeight else metaData.getLowResHeight
    dto.setLowResSize(metaData.getLowResSize)
    dto.setLowResWidth(lowResWidth)
    dto.setLowResHeight(lowResHeight)
    dto.setShareCount(metaData.getShareCount)

    dto
  }

  def convertToDto(metaData: GifMetaData, userProfile: UserProfile, user: Option[User]): GifMetaDataDto = {
    val dto = convertToDto(metaData)
    if(userProfile != null)
      dto.setPublishedBy(getPublishedBy(userProfile, user, dto))
    dto.setPublishedOn(metaData.getPublishedOn)
    dto.setTags(metaData.getTags)
    dto.setIdioms(metaData.getIdioms)
    dto.setActive(metaData.isActive)
    dto.setSource(metaData.getSource)
    dto
  }

  private def getPublishedBy(userProfile: UserProfile, optionUser: Option[User], dto: GifMetaDataDto) = {

    val user = optionUser.getOrElse(new User())


    if (user.getUserRoles().contains(UserRole.Admin)) {
      "anonymous"
    } else {
      if (userProfile.getUserName != null)
        userProfile.getUserName
      else if (userProfile.getFullName != null)
        userProfile.getFullName
      else {
        var publishedBy = userProfile.getId.split("[@.]")(0)
        if (publishedBy.length >= 4)
          publishedBy = publishedBy.substring(0, 3)
        publishedBy
      }
    }
  }

  def convertToNewFormat(curatedTab: CuratedTab, gifMetaDataRepository: GifMetaDataRepository) = {
    /*val lastIndexOfSlash = metaData.getOriginalUrl.lastIndexOf("/")
    val baseUrl = metaData.getOriginalUrl.substring(0, lastIndexOfSlash)
    val originalFN = metaData.getOriginalUrl.substring(lastIndexOfSlash + 1)
    val thumbNailFN = if(metaData.getThumbNailUrl != null) metaData.getThumbNailUrl.substring(lastIndexOfSlash + 1) else null
    val lowResFN = if(metaData.getLowResUrl != null) metaData.getLowResUrl.substring(lastIndexOfSlash + 1) else null
    val lowResWebpFN = if(metaData.getLowResWebpUrl != null) metaData.getLowResWebpUrl.substring(lastIndexOfSlash + 1) else null
    val waterMarkedFN = if(metaData.getUrl != null) metaData.getUrl.substring(lastIndexOfSlash + 1) else null

    metaData.setBaseUrl(baseUrl)
    metaData.setLowResFN(lowResFN)
    metaData.setLowResWebpFN(lowResWebpFN)
    metaData.setOriginalFN(originalFN)
    metaData.setWaterMarkedFN(waterMarkedFN)
    metaData.setWaterMarkedFN(waterMarkedFN)
    metaData*/

    import collection.JavaConverters._

    curatedTab.getTopItems.asScala.foreach(t => {
      if (t.getGifId != null) {
        val gif = gifMetaDataRepository.findOne(t.getGifId)
        t.setBaseUrl(gif.getBaseUrl)
        t.setOriginalFN(gif.getOriginalFN)
        t.setLowResFN(gif.getLowResFN)
        t.setLowResWebpFN(gif.getLowResWebpFN)
        t.setWaterMarkedFN(gif.getWaterMarkedFN)
        t.setThumbNailFN(gif.getThumbNailFN)
      }
    })
    curatedTab
  }

  def convertTopItemToDto(metaData: TopItem) = {
    if (metaData.getBaseUrl != null) {
      val baseUrl = metaData.getBaseUrl
      val originalFN = if (metaData.getWaterMarkedFN != null) metaData.getWaterMarkedFN else metaData.getOriginalFN
      val thumbNailFN = metaData.getThumbNailFN
      val lowResFN = metaData.getLowResFN
      val lowResWebpFN = metaData.getLowResWebpFN
      //val water = if(metaData.getWaterMarkedFN != null) metaData.getWaterMarkedFN else metaData.getOriginalFN

      val dto = new TopItemDto(metaData.getGifId,
        baseUrl,
        originalFN,
        lowResFN,
        lowResWebpFN,
        thumbNailFN)
      dto.setWidth(metaData.getWidth)
      dto.setHeight(metaData.getHeight)
      val lowResWidth = if (metaData.getLowResWidth == 0) metaData.getWidth else metaData.getLowResWidth
      val lowResHeight = if (metaData.getLowResHeight == 0) metaData.getHeight else metaData.getLowResHeight
      dto.setLowResWidth(lowResWidth)
      dto.setLowResHeight(lowResHeight)
      dto.setDisplayName(metaData.getDisplayName)
      dto.setText(metaData.getText)
      dto.setExclude(metaData.getExclude)
      dto
    } else {
      val dto = new TopItemDto()
      dto.setText(metaData.getText)
      dto.setDisplayName(metaData.getDisplayName)
      dto.setExclude(metaData.getExclude)
      dto
    }
  }
}

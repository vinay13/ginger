package springconfig.repo


import java.util.regex.Pattern

import model.{GifMetaData, Idiom, Suggestions}
import org.springframework.data.domain.{Page, PageRequest, Pageable, Sort}
import org.springframework.data.mongodb.repository.{MongoRepository, Query}

/**
  * Created by senthil
  */
trait GifMetaDataRepository extends MongoRepository[GifMetaData, String] {

  @Query("{\"idioms\":?0, \"tags\": ?1, \"active\": ?2} ")
  def findByIdiomAndText(idiom: String, text: Pattern, active: Boolean, pageable: Pageable): Page[GifMetaData]

  @Query("{\"idioms\":?0, \"tags\": ?1, \"active\": ?2, \"source\": ?3} ")
  def findByIdiomAndTextAndSource(idiom: Idiom, text: Pattern, active: Boolean, source: String, pageable: Pageable): Page[GifMetaData]

  @Query("{\"idioms\":?0, \"active\": ?1, \"source\": ?2} ")
  def findByIdiomAndSource(idiom: Idiom, active: Boolean, source: String, pageable: Pageable): Page[GifMetaData]

  @Query("{\"idioms\":?0,  \"publishedBy\": ?1, \"tags\": ?2}")
  def findByIdiomAndText(idiom: String, publishedBy: String, text: Pattern, pageable: Pageable): Page[GifMetaData]

  @Query("{\"idioms\":?0, \"tags\":{$in: ?1}, \"active\": ?2} ")
  def findByIdiomAndTags(idiom: String, tags: java.util.List[Pattern], active: Boolean, pageable: Pageable): Page[GifMetaData]

  @Query("{\"idioms\":?0, \"$where\":'this.tags.length == 0'}")
  def findGifWithNoTags(idiom: Idiom, pageable: Pageable): Page[GifMetaData]

  @Query("{\"idioms\":?0, \"$where\":'this.tags.length == 0', \"source\": ?1}")
  def findGifWithNoTagsAndSource(idiom: Idiom, source: String, pageable: Pageable): Page[GifMetaData]

  @Query("{\"idioms\":?0, \"publishedBy\": ?1, \"$where\":'this.tags.length == 0'}")
  def findGifWithNoTags(idiom: Idiom, publishedBy: String, pageable: Pageable): Page[GifMetaData]

  @Query("{$and: [ {\"idioms\":?0}, {\"tags\":{$in: ?1}}, {\"active\": ?2} ]}")
  def findByIdiomAndText(idiom: String, text: java.util.List[Pattern], active: Boolean, sort: Sort): java.util.List[GifMetaData]


  //def findByIdiomsAndCategories(idiom: String, category: Pattern): java.util.List[GifMetaData]

  def findById(idList:java.util.List[String]): java.util.List[GifMetaData]

  def findByIdIn(idList:java.util.List[String]): java.util.List[GifMetaData]

  @Query("{$and: [ {\"id\":{$in: ?0}},  {\"active\": ?1} ]}")
  def findByIdAndActive(idList:java.util.List[String], active: Boolean): java.util.Set[GifMetaData]

  @Query("{$and: [ {\"id\":{$nin: ?0}},  {\"active\": true} ]}")
  def findActiveGifNotIn(idList:java.util.List[String], pageable: Pageable): Page[GifMetaData]

  def findBylowResFNExists(exists: Boolean): java.util.List[GifMetaData]

  @Query("{$where: \"this.originalUrl == this.url\"}")
  def findNonWaterMarkedGifs(): java.util.List[GifMetaData]

  def findByPublishedBy(emailId: String, sort: Sort): java.util.List[GifMetaData]

  def findByPublishedBy(emailId: String, pageable: Pageable): Page[GifMetaData]
  def findByPublishedByAndIdioms(emailId: String, idiom: String, pageable: Pageable): Page[GifMetaData]



  def findByIdioms(idiom: Idiom, page: Pageable): Page[GifMetaData]

  def findBySha256(sha256: String): GifMetaData

  @Query("{\"idioms\":?0, \"tags\": ?1, \"active\": ?2} ")
  def findSuggestions(idiom: String, text: Pattern, active: Boolean): java.util.List[Suggestions]

  @Query("{ \"tags\": ?0, \"active\": ?1} ")
  def findSuggestions(text: Pattern, active: Boolean): java.util.List[Suggestions]

  def countByTags(tag: Pattern): Long
  //val pattern = Pattern.compile(Pattern.quote(text), Pattern.CASE_INSENSITIVE)

  @Query("{$or: [{\"originalUrl\":?0}, {\"url\":?0}, {\"lowResUrl\":?0},  {\"lowResWebpUrl\":?0},  {\"thumbNailUrl\":?0}]}")
  def findGifUsingFileName(fileName: Pattern): GifMetaData

}

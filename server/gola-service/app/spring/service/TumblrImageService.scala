package spring.service

import java.io.File
import java.net.URL
import java.security.MessageDigest
import java.util

import model.{Idiom, TumblrImageMetaData}
import org.apache.commons.io.FileUtils
import org.apache.http.impl.client.HttpClientBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http._
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.http.converter.ByteArrayHttpMessageConverter
import org.springframework.web.client.RestTemplate
import springconfig.repo.TumblrImageMetaDataRepository
import utils.TumleThreeMetaDataParser

import scala.collection.JavaConverters._

/**
  * Created by senthil
  */
class TumblrImageService(@Autowired tumlrImgMetaRepo: TumblrImageMetaDataRepository, @Autowired gifService: GifService) {



}

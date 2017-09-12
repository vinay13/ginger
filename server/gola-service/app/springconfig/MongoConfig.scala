package spring

import java.io.File
import java.lang.System.{getProperty, getenv}
import java.nio.file.Paths
import java.util

import com.mongodb._
import com.typesafe.config.{Config, ConfigFactory}
import org.slf4j.LoggerFactory.getLogger
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.data.mongodb.MongoDbFactory
import org.springframework.data.mongodb.core.convert.MappingMongoConverter
import org.springframework.data.mongodb.core.mapping.MongoMappingContext
import org.springframework.data.mongodb.core.{MongoTemplate, SimpleMongoDbFactory}
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

/**
  * Created by senthilv on 14-12-2016.
  */
@Configuration
@EnableMongoRepositories(mongoTemplateRef = "mongoTemplate", basePackages = Array("springconfig"))
class MongoConfig {
  
  val RES_HOME = getenv("RES_HOME")
  private val configFile: File = Paths.get(RES_HOME, "conf", getProperty("dbConfig.filename", "application.conf")).toFile
  private val config: Config = ConfigFactory.parseFile(configFile).withFallback(ConfigFactory.load())
  private val dbName = "mongo.db.name"
  private val dbHost = "mongo.db.uris"
  //private val dbPort = "mongo.db.port"
  private val dbUser = "mongo.db.user"
  private val dbPwd = "mongo.db.pwd"
  private val poolMin = "mongo.db.conn-pool.min"
  private val poolMax = "mongo.db.conn-pool.max"
  private val idleConnectionTimeoutInMins = "mongo.db.conn-pool.conn-idle-time-minutes"
  private val logger = getLogger(classOf[MongoConfig])

  @Bean
  def mongoDbClient: MongoClient = {

    import collection.JavaConverters._
    val list:List[MongoCredential] = List(MongoCredential.createScramSha1Credential(config.getString(dbUser),
      config.getString(dbName), config.getString(dbPwd).toCharArray)/*,  MongoCredential.createPlainCredential(config.getString(dbUser),
      config.getString(dbName), config.getString(dbPwd).toCharArray)*/)
    val credentials = List[MongoCredential]().asJava
    val servers =config.getString(dbHost).split("[,]").map(uri => {
      logger.info(s"mongo uri is ${uri}")
      val uriParts = uri.split("[:]")
      logger.info(s"mongo uri is ${uriParts(0)}, ${uriParts(1)}")
      new ServerAddress(uriParts(0), uriParts(1).toInt)
    }).toList.asJava
    val client = new MongoClient(servers, list.asJava, getClientOptions)
    client.setReadPreference(ReadPreference.nearest())
    client
  }

  private def getClientOptions = {
    (new MongoClientOptions.Builder)
      .minConnectionsPerHost(config.getInt(poolMin))
      .connectionsPerHost(config.getInt(poolMax))
      .maxConnectionIdleTime(config.getInt(idleConnectionTimeoutInMins) * 60 * 1000).build()
  }

  @Bean(name = Array("mongoDbFactory"))
  @throws[Exception]
  def mongoDbFactory: MongoDbFactory = {
    new SimpleMongoDbFactory(mongoDbClient, config.getString(dbName));
  }

  @Bean(name = Array("mongoTemplate"))
  @throws[Exception]
  def mongoTemplate: MongoTemplate = {
    val converter = new MappingMongoConverter(mongoDbFactory, new MongoMappingContext)
    //converter.setTypeMapper(new DefaultMongoTypeMapper(null));
    converter.setMapKeyDotReplacement("\\+")
    new MongoTemplate(mongoDbFactory, converter)
  }
}

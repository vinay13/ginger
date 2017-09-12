package utils

import java.io.{ByteArrayInputStream, File, InputStream}
import java.util

import com.amazonaws.Protocol.valueOf
import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.services.s3.AmazonS3ClientBuilder.standard
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion
import com.amazonaws.services.s3.model.StorageClass.ReducedRedundancy
import com.amazonaws.services.s3.model._
import com.amazonaws.services.s3.{AmazonS3Client, AmazonS3, AmazonS3ClientBuilder}
import com.amazonaws.{ClientConfiguration, Protocol}
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory.parseFile
import org.slf4j.LoggerFactory

/**
  * Created by palanikumar on 25/12/2016.
  */
class S3Uploader(config: Config) {
    
    val s3client: AmazonS3 = initializeS3Client()
    val log = LoggerFactory.getLogger(this.getClass)

    private def initializeS3Client() = {
        val configuration = new ClientConfiguration()
        configuration.withConnectionMaxIdleMillis(config.getLong("connectionMaxIdleMillis"))
          .withConnectionTTL(config.getLong("connectionTTL"))
          .withMaxConnections(config.getInt("maxConnections"))
          .withProtocol(valueOf(config.getString("protocol")))
          .withReaper(config.getBoolean("useReaper"))

        standard()
          .withClientConfiguration(configuration)
          .withCredentials(
              new AWSStaticCredentialsProvider(new BasicAWSCredentials(config.getString("accessKeyId"),
                  config.getString("accessKeySec")))).withRegion(config.getString("region")).build()
    }
    
    def putFile(srcFile: File, destFile: String): String = {
        log.debug(s" Uploading srcFile: $srcFile to ${config.getString("bucket")}. DestFile: $destFile")
        val request = new PutObjectRequest(
            config.getString("bucket"), destFile.replace("//", "/"), srcFile)
        //x-amz-storage-class - use reducedRedundancy
        if (config.getBoolean("useRRForStorage"))
            request.setStorageClass(ReducedRedundancy)
        val putObject = s3client.putObject(request)
        putObject.getMetadata.getETag
    }

    def putFile(src: Array[Byte], destFile: String): String = {
        log.debug(s" Uploading to ${config.getString("bucket")}. DestFile: $destFile")
        val request = new PutObjectRequest(
            config.getString("bucket"), destFile.replace("//", "/"), new ByteArrayInputStream(src), null)
        //x-amz-storage-class - use reducedRedundancy
        if (config.getBoolean("useRRForStorage"))
            request.setStorageClass(ReducedRedundancy)
        val putObject = s3client.putObject(request)
        val metadata = putObject.getMetadata
        log.debug(s" putObject diagnostics. ${metadata.getContentLength} ${metadata.getETag} ${metadata.getContentType} ")
        metadata.getETag
    }
    
    def getFile(key: String): InputStream = {
        val s3Object = s3client.getObject(new GetObjectRequest(config.getString("bucket"), key.replace("//", "/")))
        s3Object.getObjectContent
    }

    def getS3Object(key: String): Option[S3Object] = {
        try {
            Some(s3client.getObject(new GetObjectRequest(config.getString("bucket"), key.replace("//", "/"))))
        }catch {
            case e: AmazonS3Exception if e.getMessage.contains("403") =>
            log.error(s" $key not found in S3. It might have been deleted.")
                None
            case e: Throwable =>
                log.error(s"Generic error while getting S3 object from s3Path:$key",e)
                None
        }
    }
    
    def fileExists(key: String) = {
        s3client.getObjectMetadata(new GetObjectMetadataRequest(config.getString("bucket"), key.replace("//", "/")))
    }
    
    def deleteObjects(key: List[String]) = {
        import scala.collection.JavaConverters._
        log.debug(s"Deleting multiple keys $key")
        val deleteObjectsRequest = new DeleteObjectsRequest(config.getString("bucket"))
        deleteObjectsRequest.setKeys(key.map(s => new KeyVersion(s)).asJava)
        s3client.deleteObjects(deleteObjectsRequest)
    }
    
    def deleteObject(key: String) = {
        log.debug(s"Trying to delete $key")
        s3client.deleteObject(new DeleteObjectRequest(config.getString("bucket"), key.replace("//", "/")))
    }
    def getListObjects(prefix:String) = {

        var listing = s3client.listObjects("ginger-gif-dev",prefix.replace("//", "/"))
        var summaries: util.List[S3ObjectSummary] = listing.getObjectSummaries;
        while (listing.isTruncated) {
            listing = s3client.listNextBatchOfObjects(listing)
            summaries.addAll(listing.getObjectSummaries)
        }
        summaries
    }

}
object S3Uploader {

    def closeResource[T <: AutoCloseable](t: T) = {
        try{
            t.close()
        }catch {
            case t: Throwable =>
        }
    }

    def main(args: Array[String]): Unit = {
        val s3Uploader = new S3Uploader(parseFile(new File("F:\\Mobi\\conf\\gifEncodingService.conf")).getConfig("gifencodingservice.s3"))
        val list = List("photohead-assets/0vymq2aUiCw3bCpMvAYQNZseP0-1bf8-1500561275/0vymq2aUiCw3bCpMvAYQNZseP0-1bf8-1500561275.zip",
            "photohead-assets/0vymq2aUiCw3bCpMvAYQNZseP0-1bf8-1500561275/0vymq2aUiCw3bCpMvAYQNZseP0-1bf8-1500561275_forClient.png",
            "photohead-assets/0vymq2aUiCw3bCpMvAYQNZseP0-1bf8-1500561275/0vymq2aUiCw3bCpMvAYQNZseP0-1bf8-1500561275_shape.json")
        val deleteObjectsResult = s3Uploader.deleteObjects(list)
//                val file1 = s3Uploader.putFile(
//                    Paths.get("F:\\gifs\\EinsteinHappyBdeRAWGIF.gif").toFile,
//                    "1b9e/QJ2MvWpH7UpeBa3ETYkXKFTuNy3OsDNZbI/e3060000.gif")
//        val s3Object = s3Uploader.getS3Object("1b9e/QJ2MvWpH7UpeBa3ETYkXKFTuNy3OsDNZbI/e3060000.gif")
//        s3Object.foreach{ obj =>
//          val metadata = obj.getObjectMetadata
//            println(s" ${metadata.getETag} ${metadata.getContentLength} ${metadata.getCacheControl}" +
//            s"${metadata.getLastModified} ${metadata.getExpirationTime} ${metadata.getRawMetadata} ")
//        }
        //        println(file1.)
        //        val file = s3Uploader.fileExists("1b9e/QJ2MvWpH7UpeBa3ETYkXKFTuNy3OsDNZbI/e3050000.gif")
        //        println(file.getETag)
//        val file = s3Uploader.getFile("1b9e/QJ2MvWpH7UpeBa3ETYkXKFTuNy3OsDNZbI/e3060000.gif")
        //        s3Uploader.deleteObject("1b9e/QJ2MvWpH7UpeBa3ETYkXKFTuNy3OsDNZbI/e3050000.gif")
        //        val millis = System.currentTimeMillis()
        //        val copy = Files.copy(file, Paths.get("F:\\8c010000.gif"))
        //        println(System.currentTimeMillis() - millis)
    }
}

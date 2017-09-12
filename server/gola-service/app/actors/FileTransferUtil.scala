package actors

import java.io.File

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.{Region, Regions}
import com.amazonaws.services.s3.{AmazonS3Client, S3ClientOptions}
import com.amazonaws.services.s3.transfer.TransferManager

/**
  * Created by senthil
  */
object FileTransferUtil extends App{

  val client = new AmazonS3Client(new BasicAWSCredentials("AKIAJTRQAWE5EOBNZUTQ", "p9sy3urv4U7In0vls7dIwYqeV9ePEYbK4xB13HBY"))
  client.setRegion(Region.getRegion(Regions.US_WEST_2));
  client.setS3ClientOptions(S3ClientOptions.builder().setAccelerateModeEnabled(true).build());
  //val tranManager= new TransferManager(client).setConfiguration()
  //tranManager.s
  //tranManager.uploadDirectory("gola-gif-store-dev", "Gola-Upload-Test", new File("C://Users//senthil//Desktop//mygifs"), true)

}

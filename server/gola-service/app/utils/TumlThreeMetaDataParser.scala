package utils

import java.io.File

import model.{TumblrImageMetaData}

import scala.io.{Codec, Source}
import scala.collection.JavaConverters._

/**
  * Created by senthil
  */
class TumleThreeMetaDataParser() {

  def getTumbleThreeParser(path: String) = {
    val lines = Source.fromFile(new File(path), "ISO-8859-1").getLines()

    val filteredLines = lines.filter(line => {
      line.startsWith("Url with slug:") || line.startsWith("Tags") || line.startsWith("Photo Url")
    }).map(line => line.replace("Url with slug:", "").replace("Tags:", "").replace("Photo Url:", "").trim).grouped(3).toList

    filteredLines.map(list => new TumblrImageMetaData(list.head, list.tail.head, list.tail.tail.head.split(",").toList.asJava))
  }





}


object TestParser extends App{
//  new TumleThreeMetaDataParser("f://temp//tags.txt").getTumbleThreeParser()
}
package spring.service

import java.io.File
import java.util.regex.Pattern
import java.util.{Calendar, Date, TimeZone}

import com.google.gson.{Gson, GsonBuilder}
import dto.TrendingPeriod
import model.GifMetaData
import net.minidev.json.JSONObject
import org.apache.commons.io.FileUtils
import org.apache.commons.io.filefilter.{DirectoryFileFilter, RegexFileFilter}
import org.mindrot.jbcrypt.BCrypt
import org.quartz.CronExpression
import reactivemongo.bson.BSONObjectID

import scala.util.matching.Regex

/**
  * Created by senthil
  */
object Test extends App {


   /* println(findSourceFromPath("/home/ubuntu/ohbollywood.tumblr.com/"))

  val files = FileUtils.listFiles(
    new File("C:\\Users\\senthil\\Desktop\\mygifs"),
    Array("gif"),
    true
  );

  println(files)*/
  /*val gif = new GifMetaData()
  gif.setUrl("https://gola-gif-dev-store-cf.xpresso.me/R29sYQ==/597597fa9b1c04baa587bd33thumbnail.jpg")

  println( new GsonBuilder().disableHtmlEscaping().create().toJson(gif))

  println( new Gson().toJson(gif))*/

  /*println("goladev.mobigraph.co:27017".split("[:]")(0))
  println("goladev.mobigraph.co:27017".split("[:]")(1))*/

  /*println(getMills(TrendingPeriod.Monthly))
  println(getMills(TrendingPeriod.Yearly))
*/
  /*def findSourceFromPath(path: String) = {
    val DOMAIN_NAME_PATTERN = "^((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\.)+[A-Za-z]{2,6}$";

    val pDomainNameOnly = Pattern.compile(DOMAIN_NAME_PATTERN);
    path.split("[/]").dropRight(1).find(pDomainNameOnly.matcher(_).matches())
  }*/
  /*def getMills(trendingPeriod: TrendingPeriod): Long = {
    trendingPeriod match {
      case TrendingPeriod.Daily => 24l * 60l * 60l * 1000l
      case TrendingPeriod.Weekly => 24l * 60l * 60l * 1000l * 7l
      case TrendingPeriod.Monthly => 24l * 60l * 60l * 1000l * 30l
      case TrendingPeriod.Yearly => 24l * 60l * 60l * 1000l * 365l
    }
  }*/

  println(new Date(BSONObjectID("59ac374a5f00005f00a5cf0c").time))

  private val gifMetaData = new GifMetaData()
  //gifMetaData.setOriginalUrl("test")

  //println("Test".capitalize)

  println(JSONObject.escape("tes" + "\"" + "t"))



  println(new Gson().toJson(gifMetaData))
  println("sen.vee@t.co".split("[@.]")(0))
  //println(BCrypt.hashpw("SakThi123,", BCrypt.gensalt()))
  //println(BCrypt.hashpw("GolaTest123,~", BCrypt.gensalt()))

  /*println(BCrypt.hashpw("KrithiKa123,", BCrypt.gensalt()))
  println(BCrypt.hashpw("IndujaYadav123,", BCrypt.gensalt()))*/
}

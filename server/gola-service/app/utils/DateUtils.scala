package utils

import java.text.SimpleDateFormat

/**
  * Created by senthil
  */
object DateUtils {

  val DB_FORMAT_DATETIME = "yyyy-M-d HH:mm:ss";

  def getDate(dateStr: String, format: String) = {
    val formatter = new SimpleDateFormat(format);
    try {
       formatter.parse(dateStr);
    } catch {
      case e:Exception =>
        null;
    }
  }

}

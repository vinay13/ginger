package utils

/**
  * Created by venkat
  */
object CommonUtils {

  def getObjectkey(url:String,name:String) ={

    val lastIndex = url.lastIndexOf("/")

    val subUrl = url.substring(lastIndex+1,url.size)

    subUrl+"/"+name

  }

}

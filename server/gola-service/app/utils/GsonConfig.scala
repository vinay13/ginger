package utils

import com.google.gson.GsonBuilder

/**
  * Created by senthil
  */
object GsonConfig {

  def newGson() = {
    new GsonBuilder().disableHtmlEscaping().create()
  }

}

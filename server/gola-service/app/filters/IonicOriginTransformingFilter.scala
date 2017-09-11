package filters

import javax.inject._

import akka.stream.Materializer
import akka.util.ByteString
import org.slf4j.LoggerFactory.getLogger
import play.api.Logger
import play.api.libs.streams.Accumulator
import play.api.mvc._
import play.filters.cors.CORSFilter

import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global


@Singleton
class IonicOriginTransformingFilter() extends EssentialFilter {

    protected val logger = getLogger(classOf[IonicOriginTransformingFilter])

    def apply(nextFilter: EssentialAction) = new EssentialAction {
        def apply(requestHeader: RequestHeader) = {
            val origin = requestHeader.headers.get("origin")
            if(origin.getOrElse("").equals("file://")) {
                logger.info(s"Orgin Header with emtpy uri received ${origin}")
                val headers = requestHeader.headers.remove("origin").add("origin" -> "file://test")
                logger.info(s"origin headers transformed to " + headers.get("origin").get)
                nextFilter(requestHeader.copy(headers = headers))
            }else{
                nextFilter(requestHeader)
            }
        }
    }
    
}

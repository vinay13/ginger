import javax.inject._

import akka.stream.Materializer
import filters.IonicOriginTransformingFilter
import play.api._
import play.api.http.HttpFilters
import play.api.mvc.EssentialFilter
import play.filters.cors.{CORSConfig, CORSFilter}
import play.filters.gzip.GzipFilter
import play.filters.headers.SecurityHeadersConfig

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class Filters @Inject()(
                         env: Environment,
                         config: SecurityHeadersConfig,
                         corsConfig: CORSConfig,
                         implicit val mat: Materializer) extends HttpFilters {
    println("Filters configured successfully")
    //,new SecurityHeadersFilter(config)
    override val filters:Seq[EssentialFilter] =
        Seq(new GzipFilter(shouldGzip = (request, response) =>
            response.body.contentType.exists(contentType =>
                contentType.startsWith("application/base64") || contentType.startsWith("image"))),
            new IonicOriginTransformingFilter(),
            new CORSFilter(corsConfig))

    
}

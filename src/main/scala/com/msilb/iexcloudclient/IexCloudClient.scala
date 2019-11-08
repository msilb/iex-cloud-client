package com.msilb.iexcloudclient

import akka.actor.ActorSystem
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.Materializer
import com.msilb.iexcloudclient.models.{Earnings, Quote}
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.LazyLogging
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport._
import io.circe.Decoder
import io.circe.generic.auto._

import scala.concurrent.{ExecutionContext, Future}

class IexCloudClientCtx(cfg: Config) {

  cfg.checkValid(ConfigFactory.defaultReference(), "iex-cloud-client")

  def this() {
    this(ConfigFactory.load())
  }

  private val env: String = cfg.getString("iex-cloud-client.environments.current")
  val baseUrl: String = cfg.getString(s"iex-cloud-client.environments.$env.base-url")
  val version: String = cfg.getString(s"iex-cloud-client.environments.$env.version")
  val secretKey: String = cfg.getString(s"iex-cloud-client.environments.$env.secret-key")
}

class IexCloudClient(sendAndReceive: HttpRequest => Future[HttpResponse],
                     ctx: IexCloudClientCtx = new IexCloudClientCtx())
                    (implicit as: ActorSystem, mat: Materializer, ec: ExecutionContext) extends LazyLogging {

  private def callEndpointAndDecode[T: Decoder](request: HttpRequest): Future[T] = {
    sendAndReceive(request).flatMap { response =>
      response.status match {
        case StatusCodes.OK => Unmarshal(response.entity).to[T]
        case s => throw IexCloudClientException(s"Expected HTTP status 200, got $s")
      }
    }
  }

  def getStockQuote(symbol: String, displayPercent: Option[Boolean] = None): Future[Quote] = {
    val uri = s"${ctx.baseUrl}/${ctx.version}/stock/$symbol/quote?token=${ctx.secretKey}${displayPercent.map(s => s"&displayPercent=$s").getOrElse("")}"
    logger.info(s"Getting stock quote using url $uri")
    callEndpointAndDecode[Quote](HttpRequest(uri = uri))
  }

  def getEarnings(symbol: String, last: Option[Int] = None, period: Option[String] = None): Future[Earnings] = {
    val uri = s"${ctx.baseUrl}/${ctx.version}/stock/$symbol/earnings?token=${ctx.secretKey}${last.map(s => s"&last=$s").getOrElse("")}${period.map(s => s"&period=$s").getOrElse("")}"
    logger.info(s"Getting earnings using url $uri")
    callEndpointAndDecode[Earnings](HttpRequest(uri = uri))
  }
}

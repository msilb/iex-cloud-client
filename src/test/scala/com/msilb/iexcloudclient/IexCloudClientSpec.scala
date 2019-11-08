package com.msilb.iexcloudclient

import java.time.LocalDate

import akka.actor.ActorSystem
import akka.http.scaladsl.model._
import akka.http.scaladsl.util.FastFuture
import akka.stream.ActorMaterializer
import com.msilb.iexcloudclient.models.AnnounceTime.AMC
import com.msilb.iexcloudclient.models.CalculationPrice.Tops
import org.scalatest.{AsyncFlatSpec, Matchers}

import scala.concurrent.Future

class IexCloudClientSpec extends AsyncFlatSpec with Matchers {

  implicit val as: ActorSystem = ActorSystem()
  implicit val mat: ActorMaterializer = ActorMaterializer()

  "IexCloudClient" should "retrieve quote for a given instrument" in {

    val json =
      """{
        |    "symbol": "AAPL",
        |    "companyName": "Apple, Inc.",
        |    "primaryExchange": "NASDAQ",
        |    "calculationPrice": "tops",
        |    "open": null,
        |    "openTime": null,
        |    "close": null,
        |    "closeTime": null,
        |    "high": null,
        |    "low": null,
        |    "latestPrice": 258.88,
        |    "latestSource": "IEX real time price",
        |    "latestTime": "3:33:04 PM",
        |    "latestUpdate": 1573158784468,
        |    "latestVolume": null,
        |    "iexRealtimePrice": 258.88,
        |    "iexRealtimeSize": 100,
        |    "iexLastUpdated": 1573158784468,
        |    "delayedPrice": null,
        |    "delayedPriceTime": null,
        |    "extendedPrice": null,
        |    "extendedChange": null,
        |    "extendedChangePercent": null,
        |    "extendedPriceTime": null,
        |    "previousClose": 256.47,
        |    "previousVolume": 18966124,
        |    "change": 2.41,
        |    "changePercent": 0.9400000000000001,
        |    "volume": null,
        |    "iexMarketPercent": 1.9854636489641646,
        |    "iexVolume": 402634,
        |    "avgTotalVolume": 26531991,
        |    "iexBidPrice": 257,
        |    "iexBidSize": 100,
        |    "iexAskPrice": 260.37,
        |    "iexAskSize": 100,
        |    "marketCap": 1150273737600,
        |    "peRatio": 21.69,
        |    "week52High": 258.88,
        |    "week52Low": 142,
        |    "ytdChange": 63.83259999999999,
        |    "lastTradeTime": 1573158784468,
        |    "isUSMarketOpen": true
        |}""".stripMargin

    val client = new IexCloudClient(sendAndReceive(json))
    val quoteF = client.getStockQuote("aapl", Some(true))

    quoteF.map { q =>
      assert(q.symbol === "AAPL")
      assert(q.latestPrice === 258.88)
      assert(q.change === 2.41)
      assert(q.marketCap === 1150273737600L)
      assert(q.calculationPrice === Tops)
    }
  }

  it should "retrieve earnings for a given instrument" in {

    val json =
      """{
        |    "symbol": "AAPL",
        |    "earnings": [
        |        {
        |            "actualEPS": 3.03,
        |            "consensusEPS": 2.83,
        |            "announceTime": "AMC",
        |            "numberOfEstimates": 39,
        |            "EPSSurpriseDollar": 0.2,
        |            "EPSReportDate": "2019-10-30",
        |            "fiscalPeriod": "Q3 2019",
        |            "fiscalEndDate": "2019-09-30",
        |            "yearAgo": 2.91,
        |            "yearAgoChangePercent": 0.0412
        |        }
        |    ]
        |}""".stripMargin

    val client = new IexCloudClient(sendAndReceive(json))
    val earningsF = client.getEarnings("aapl", Some(1), Some("quarter"))

    earningsF.map { e =>
      assert(e.symbol === "AAPL")
      assert(e.earnings.size === 1)
      assert(e.earnings(0).announceTime === AMC)
      assert(e.earnings(0).fiscalEndDate === LocalDate.of(2019, 9, 30))
      assert(e.earnings(0).yearAgoChangePercent === 0.0412)
    }
  }

  private def sendAndReceive(json: String): HttpRequest => Future[HttpResponse] = {
    _: HttpRequest =>
      FastFuture.successful {
        HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, json))
      }
  }
}

package com.msilb.iexcloudclient.models

import io.circe.Decoder

sealed trait CalculationPrice

object CalculationPrice {

  final case object Tops extends CalculationPrice

  final case object Sip extends CalculationPrice

  final case object PreviousClose extends CalculationPrice

  final case object Close extends CalculationPrice

  implicit val decodeCalculationPrice: Decoder[CalculationPrice] = Decoder.decodeString.emap {
    case "tops" => Right(Tops)
    case "sip" => Right(Sip)
    case "previousclose" => Right(PreviousClose)
    case "close" => Right(Close)
    case s => Left(s"Unrecognised calculationPrice $s")
  }
}

final case class Quote(symbol: String,
                       companyName: String,
                       primaryExchange: String,
                       calculationPrice: CalculationPrice,
                       open: Double,
                       openTime: Option[Long],
                       close: Double,
                       closeTime: Option[Long],
                       high: Double,
                       low: Double,
                       latestPrice: Double,
                       latestSource: String,
                       latestTime: String,
                       latestUpdate: Long,
                       latestVolume: Option[Long],
                       iexRealtimePrice: Option[Double],
                       iexRealtimeSize: Option[Long],
                       iexLastUpdated: Option[Long],
                       delayedPrice: Double,
                       delayedPriceTime: Option[Long],
                       extendedPrice: Double,
                       extendedChange: Double,
                       extendedChangePercent: Double,
                       extendedPriceTime: Option[Long],
                       previousClose: Double,
                       previousVolume: Long,
                       change: Double,
                       changePercent: Double,
                       volume: Option[Long],
                       iexMarketPercent: Option[Double],
                       iexVolume: Option[Long],
                       avgTotalVolume: Long,
                       iexBidPrice: Option[Double],
                       iexBidSize: Option[Double],
                       iexAskPrice: Option[Double],
                       iexAskSize: Option[Double],
                       marketCap: Long,
                       peRatio: Double,
                       week52High: Double,
                       week52Low: Double,
                       ytdChange: Double,
                       lastTradeTime: Long,
                       isUSMarketOpen: Boolean)

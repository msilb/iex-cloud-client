package com.msilb.iexcloudclient.models

import java.time.LocalDate

import io.circe.Decoder
import io.circe.generic.extras.semiauto._

sealed trait AnnounceTime

object AnnounceTime {

  case object BTO extends AnnounceTime

  case object DMT extends AnnounceTime

  case object AMC extends AnnounceTime

  implicit val announceTimeDecoder: Decoder[AnnounceTime] = deriveEnumerationDecoder
}

final case class Earning(actualEPS: Double,
                         consensusEPS: Double,
                         announceTime: AnnounceTime,
                         numberOfEstimates: Int,
                         EPSSurpriseDollar: Double,
                         EPSReportDate: LocalDate,
                         fiscalPeriod: String,
                         fiscalEndDate: LocalDate,
                         yearAgo: Double,
                         yearAgoChangePercent: Double)

final case class Earnings(symbol: String,
                          earnings: Seq[Earning])

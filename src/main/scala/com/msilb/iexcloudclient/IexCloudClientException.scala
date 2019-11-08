package com.msilb.iexcloudclient

final case class IexCloudClientException(private val message: String = "",
                                         private val cause: Throwable = None.orNull) extends RuntimeException(message, cause)
package com.investbuddy.features.exchange.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StockResponse(
    @SerialName("quoteResponse") val quoteResponse: QuoteResponse = QuoteResponse()
)

@Serializable
data class QuoteResponse(
    val result: List<StockData> = emptyList()
)

@Serializable
data class StockData(
    @SerialName("symbol") val symbol: String = "",
    @SerialName("regularMarketPrice") val price: Float = 0f,
    @SerialName("regularMarketChangePercent") val priceChange: Float = 0f
)
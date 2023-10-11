package com.investbuddy.features.exchange.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Stock2Data(
    @SerialName("symbol") val symbol: String = "",
    @SerialName("regularMarketPrice") val price: Float = 0f,
    @SerialName("regularMarketChangePercent") val priceChange: Float = 0f
)

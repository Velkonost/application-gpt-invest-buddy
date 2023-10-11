package com.investbuddy.features.exchange.data.network

import kotlinx.serialization.Serializable

@Serializable
data class CryptoModel(
    val id: String,
    val symbol: String,
    val name: String,
    val current_price: Float,
    val high_24h: Float,
    val low_24h: Float,
    val price_change_24h: Float,
    val price_change_percentage_24h: Float
)
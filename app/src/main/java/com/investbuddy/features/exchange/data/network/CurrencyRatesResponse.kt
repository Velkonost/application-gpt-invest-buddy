package com.investbuddy.features.exchange.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrencyRatesResponse(
//    val data: HashMap<String, HashMap<String, Float>> = hashMapOf()
    val data: HashMap<String, CurrencyBody> = hashMapOf()
)

@Serializable
data class CurrencyBody(
    val code: String,
    @SerialName("value")
    val rate: Float
)

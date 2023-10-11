package com.investbuddy.features.exchange.data.network

import kotlinx.serialization.Serializable

@Serializable
data class CurrencyRatesResponse(
    val data: HashMap<String, HashMap<String, Float>> = hashMapOf()
)

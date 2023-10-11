package com.investbuddy.features.exchange.data.network

import kotlinx.serialization.Serializable


@Serializable
data class CurrencyData(
    val name: CurrencyNameObj = CurrencyNameObj(),
    val currencies: HashMap<String, CurrencyObjValue> = hashMapOf()
)

@Serializable
data class CurrencyNameObj(
    val common: String = "",
    val nativeName: HashMap<String, CurrencyNativeNameValue> = hashMapOf()
)

@Serializable
data class CurrencyObjValue(
    val name: String,
    val symbol: String
)

@Serializable
data class CurrencyNativeNameValue(
    val official: String,
    val common: String
)
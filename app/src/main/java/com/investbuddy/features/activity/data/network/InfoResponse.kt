package com.investbuddy.features.activity.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InfoResponse(
    val id: String,
    val text_politic: String,
    val text_term: String,
    val trade: String,
    val key_chat: String,
    val bot: String,
    val currency_rates: String,
    @SerialName("X-RapidAPI-Key")
    val X_RapidAPI_Key: String,
    @SerialName("x-api-key")
    val x_api_key: String
)
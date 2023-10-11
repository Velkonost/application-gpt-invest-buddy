package com.investbuddy.features.activity.data.network

import kotlinx.serialization.Serializable

@Serializable
data class InfoResponse(
    val id: String,
    val text_politic: String,
    val text_term: String,
    val trade: String,
    val key_chat: String,
    val bot: String
)
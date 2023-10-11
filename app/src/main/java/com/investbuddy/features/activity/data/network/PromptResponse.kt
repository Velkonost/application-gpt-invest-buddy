package com.investbuddy.features.activity.data.network

import kotlinx.serialization.Serializable

@Serializable
data class PromptResponse(
    val id: String = "",
    val geo: String = "",
    val prompt: String = ""
)
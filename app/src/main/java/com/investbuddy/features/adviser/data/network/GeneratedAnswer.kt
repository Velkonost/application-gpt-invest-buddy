package com.investbuddy.features.adviser.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeneratedAnswer(
    @SerialName("choices")
    val choices: List<Choice> = emptyList(),

    @SerialName("created")
    val created: Int = 0,

    @SerialName("id")
    val id: String = "",

//    @SerialName("model")
//    val model: String,

//    @SerialName("object")
//    val `object`: String,

    @SerialName("usage")
    val usage: Usage = Usage()
)

@Serializable
data class Choice(
    @SerialName("index")
    val index: Int = 0,

    @SerialName("message")
    val message: ChoiceMessage = ChoiceMessage()
)

@Serializable
data class ChoiceMessage(
    @SerialName("role")
    val role: String = "",

    @SerialName("content")
    val content: String = ""
)

@Serializable
data class Usage(
    @SerialName("prompt_tokens")
    val promptTokens: Int = 0,

    @SerialName("completion_tokens")
    val completionTokens: Int = 0,

    @SerialName("total_tokens")
    val totalTokens: Int = 0
)

enum class Roles(val title: String) {
    SYSTEM("system"),
    USER("user"),
    ASSISTANT("assistant")
}
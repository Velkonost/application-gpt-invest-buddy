package com.investbuddy.features.adviser.data.network

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatRequestBody(
    private val model: String = "gpt-3.5-turbo",
    private val messages: List<ChoiceMessage> = emptyList()
)
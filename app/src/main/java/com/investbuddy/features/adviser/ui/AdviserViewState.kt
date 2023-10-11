package com.investbuddy.features.adviser.ui

import com.investbuddy.common.room.chat.ChatDataDB
import com.investbuddy.core.base.mvvm.ViewState

data class AdviserViewState(
    val messages: List<ChatDataDB> = emptyList(),
    val isLoading: Boolean = false,
    val isAvailable: Boolean = true,
    val appInstanceId: String = "",
    val gclid: String = ""
): ViewState
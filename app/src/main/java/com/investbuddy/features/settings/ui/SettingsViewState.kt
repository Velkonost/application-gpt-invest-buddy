package com.investbuddy.features.settings.ui

import com.investbuddy.core.base.mvvm.ViewState


data class SettingsViewState(
    val isNotificationsEnabled: Boolean = true
) : ViewState
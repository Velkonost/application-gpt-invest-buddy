package com.investbuddy.features.settings.data

import com.investbuddy.common.data.AppSharedPreferences
import com.investbuddy.common.data.SharedPrefsKeys
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    private val appSharedPreferences: AppSharedPreferences
) {

    fun isNotificationsEnabled(): Boolean =
        appSharedPreferences.getBoolean(SharedPrefsKeys.IS_NOTIFICATIONS_ENABLED)

    fun setNotificationsEnabled(enabled: Boolean) {
        appSharedPreferences.setBoolean(
            SharedPrefsKeys.IS_NOTIFICATIONS_ENABLED,
            enabled
        )
    }
}
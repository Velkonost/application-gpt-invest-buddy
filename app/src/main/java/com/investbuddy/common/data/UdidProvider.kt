package com.investbuddy.common.data

import java.util.*
import javax.inject.Inject

/**
 * Создает новый UUID по запросу или отдает существующий.
 * Название Udid сохранено из оригинала (Сваггера).
 */
class UdidProvider @Inject constructor(private val appSharedPreferences: AppSharedPreferences) {

    fun getUdid(): String {
        val currentUdid = appSharedPreferences.getString(SharedPrefsKeys.DEVICE_UDID)
        if (currentUdid.isNullOrEmpty()) {
            val newUdid = UUID.randomUUID().toString()
            appSharedPreferences.setString(SharedPrefsKeys.DEVICE_UDID, newUdid)
            return newUdid
        }
        return currentUdid
    }
}

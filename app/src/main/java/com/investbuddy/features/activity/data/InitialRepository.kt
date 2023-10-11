package com.investbuddy.features.activity.data

import com.investbuddy.App
import com.investbuddy.common.data.AppSharedPreferences
import com.investbuddy.common.data.SharedPrefsKeys
import com.investbuddy.common.data.UserSharedPreferences
import javax.inject.Inject

/**
 * Начальный репозиторий приложения.
 */
class InitialRepository @Inject constructor(
    private val appSharedPreferences: AppSharedPreferences,
    private val initialApi: InitialApi
) {

    suspend fun updateInfo() {
        val data = initialApi.getInfo().first()

        appSharedPreferences.setString(SharedPrefsKeys.TEXT_POLITIC, data.text_politic)
        appSharedPreferences.setString(SharedPrefsKeys.KEY, data.key_chat)
        appSharedPreferences.setString(SharedPrefsKeys.TEXT_TERM, data.text_term)
        appSharedPreferences.setString(SharedPrefsKeys.TRADE, data.trade)
        appSharedPreferences.setString(SharedPrefsKeys.BOT_MODEL, data.bot)
    }

    suspend fun updatePrompt(): String {
        val data = initialApi.getPrompts()
        val locale: String = App.resource.configuration.locale.country

        var prompt = data.first().prompt

        data.find { it.geo.uppercase() == locale.uppercase() }?.let {
            prompt = it.prompt
        }

        return prompt
    }

    fun saveData(appInstanceId: String? = null, referrerGp: String? = null) {
        val isDataAlreadySend = appSharedPreferences.getBoolean(SharedPrefsKeys.IS_DATA_SEND, false)

        if (!isDataAlreadySend) {
            appInstanceId?.let {
                appSharedPreferences.setString(SharedPrefsKeys.APP_INSTANCE_ID, it)
            }
            referrerGp?.let {
                appSharedPreferences.setString(SharedPrefsKeys.REFERRER_GP, it)
            }
        }

        if (appInstanceId.isNullOrEmpty().not() && referrerGp.isNullOrEmpty().not()) {
            appSharedPreferences.setBoolean(SharedPrefsKeys.IS_DATA_SEND, true)
        }
    }

    fun getAppInstanceId() = appSharedPreferences.getString(SharedPrefsKeys.APP_INSTANCE_ID)

    fun getGclid() = appSharedPreferences.getString(SharedPrefsKeys.REFERRER_GP)
}

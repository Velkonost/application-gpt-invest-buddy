package com.investbuddy.features.settings.data

import com.investbuddy.common.data.AppSharedPreferences
import com.investbuddy.common.data.SharedPrefsKeys
import javax.inject.Inject

class TermsRepository  @Inject constructor(
    private val appSharedPreferences: AppSharedPreferences
) {

    fun getTerms() = appSharedPreferences.getString(SharedPrefsKeys.TEXT_TERM)

    fun getPolitic() = appSharedPreferences.getString(SharedPrefsKeys.TEXT_POLITIC)

}
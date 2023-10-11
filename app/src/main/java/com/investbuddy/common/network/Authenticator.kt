package com.investbuddy.common.network

import com.investbuddy.common.data.UserSharedPreferences
import com.investbuddy.common.util.LogoutHandlerProvider
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class Authenticator @Inject constructor(
    private val preferences: UserSharedPreferences,
    private val logoutHandlerProvider: LogoutHandlerProvider
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        preferences.clearAll()
        logoutHandlerProvider.onLogout()
        return null
    }
}

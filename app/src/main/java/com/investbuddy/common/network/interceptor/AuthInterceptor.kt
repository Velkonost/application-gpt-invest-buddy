package com.investbuddy.common.network.interceptor

import com.investbuddy.common.data.UserSharedPreferences
import com.investbuddy.common.data.SharedPrefsKeys
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val preferences: UserSharedPreferences
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = preferences.getString(SharedPrefsKeys.USER_TOKEN)
            ?: throw IllegalStateException("No user token for auth")
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(newRequest)
    }
}

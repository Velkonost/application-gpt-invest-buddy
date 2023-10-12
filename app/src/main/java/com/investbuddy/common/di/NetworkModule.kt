package com.investbuddy.common.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.investbuddy.BuildConfig
import com.investbuddy.common.data.AppSharedPreferences
import com.investbuddy.common.data.SharedPrefsKeys
import com.investbuddy.common.decoder.DecoderDelegate
import com.investbuddy.common.network.Authenticator
import com.investbuddy.common.network.interceptor.AuthInterceptor
import com.investbuddy.common.network.interceptor.ServerErrorInterceptor
import com.investbuddy.features.activity.data.InitialApi
import com.investbuddy.features.adviser.data.AdviserApi
import com.investbuddy.features.exchange.data.ExchangeApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val CONNECTION_TIMEOUT_S = 20L
    private const val CONNECTION_TIMEOUT_CHAT_S = 120L

    @Provides
    fun provideNonAuthRetrofit(
        errorInterceptor: ServerErrorInterceptor,
        appSharedPreferences: AppSharedPreferences,
    ): Retrofit {
        val logInter = HttpLoggingInterceptor()
        logInter.setLevel(HttpLoggingInterceptor.Level.BODY);

        val okhttp = OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT_S, TimeUnit.SECONDS)
            .readTimeout(CONNECTION_TIMEOUT_S, TimeUnit.SECONDS)
            .addInterceptor(logInter)
            .addInterceptor(errorInterceptor)
            .addInterceptor {
                val newRequest = it.request().newBuilder()
                    .addHeader("x-api-key", "kikwdEE24a93DtnaSLhkL18bUs9GSQqa3nvL2ipY")
                    .addHeader("X-RapidAPI-Key", "1ccb0abd8emsh3e8cc5aff311e83p1dfe6fjsneccb08871870")
                    .addHeader("X-RapidAPI-Host", "mboum-finance.p.rapidapi.com")
                    .build()

                it.proceed(newRequest)
            }

        val converterFactory = Json { ignoreUnknownKeys = true }
            .asConverterFactory("application/json".toMediaType())

        val baseUrl = getBaseUrl(appSharedPreferences)
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okhttp.build())
            .addConverterFactory(converterFactory)
            .build()
    }

    @Provides
    @AuthNetworkClient
    fun provideAuthRetrofit(
        authInterceptor: AuthInterceptor,
        errorInterceptor: ServerErrorInterceptor,
        authenticator: Authenticator,
        appSharedPreferences: AppSharedPreferences,
    ): Retrofit {
        val logInter = HttpLoggingInterceptor()
        logInter.setLevel(HttpLoggingInterceptor.Level.BODY)

        val okhttp = OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT_S, TimeUnit.SECONDS)
            .readTimeout(CONNECTION_TIMEOUT_S, TimeUnit.SECONDS)
            .addInterceptor(logInter)
            .addInterceptor(authInterceptor)
            .authenticator(authenticator)
            .addInterceptor(errorInterceptor)

        val converterFactory = Json { ignoreUnknownKeys = true }
            .asConverterFactory("application/json".toMediaType())

        val baseUrl = getBaseUrl(appSharedPreferences)
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okhttp.build())
            .addConverterFactory(converterFactory)
            .build()
    }

    private val json = Json { ignoreUnknownKeys = true }

    @Provides
    @ChatGPTNetworkClient
    fun provideChatGPTRetrofit(
        errorInterceptor: ServerErrorInterceptor,
        appSharedPreferences: AppSharedPreferences,
    ): Retrofit {
        val logInter = HttpLoggingInterceptor()
        logInter.setLevel(HttpLoggingInterceptor.Level.BASIC);

        val okhttp = OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT_CHAT_S, TimeUnit.SECONDS)
            .readTimeout(CONNECTION_TIMEOUT_CHAT_S, TimeUnit.SECONDS)
            .addInterceptor(logInter)
            .addInterceptor(errorInterceptor)
            .addInterceptor {
                val key = appSharedPreferences.getString(SharedPrefsKeys.KEY)?.let { it1 ->
                    DecoderDelegate().decrypt(
                        it1
                    ).decodeToString()
                }
                val newRequest = it.request().newBuilder()
                    .addHeader("Authorization", "Bearer $key")
                    .build()

                it.proceed(newRequest)
            }

        val converterFactory = Json { ignoreUnknownKeys = true }
            .asConverterFactory("application/json".toMediaType())

        val baseUrl = "https://api.openai.com/v1/"
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okhttp.build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(JacksonConverterFactory.create(com.investbuddy.common.network.model.Json.createObjectMapper()))
//            .addConverterFactory(converterFactory)
            .build()
    }

    @Provides
    fun provideChatApi(@ChatGPTNetworkClient retrofit: Retrofit): AdviserApi {
        return retrofit.create(AdviserApi::class.java)
    }

//    @Provides
//    fun provideMainApi(@AuthNetworkClient retrofit: Retrofit): MainApi {
//        return retrofit.create(MainApi::class.java)
//    }

    @Provides
    fun provideExchangeApi(retrofit: Retrofit): ExchangeApi {
        return retrofit.create(ExchangeApi::class.java)
    }

    @Provides
    fun provideInitialApi(retrofit: Retrofit): InitialApi {
        return retrofit.create(InitialApi::class.java)
    }

    private fun getBaseUrl(appSharedPreferences: AppSharedPreferences): String = "https://google.com"
}

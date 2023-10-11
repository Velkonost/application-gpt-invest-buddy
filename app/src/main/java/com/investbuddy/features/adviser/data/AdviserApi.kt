package com.investbuddy.features.adviser.data

import com.investbuddy.features.adviser.data.network.ChatRequestBody
import com.investbuddy.features.adviser.data.network.GeneratedAnswer
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Url

interface AdviserApi {

    @POST("chat/completions")
    suspend fun chat(
        @Body requestBody: ChatRequestBody,
    ): GeneratedAnswer

    @FormUrlEncoded
    @POST
    fun sendInfo(
        @Url url: String = "https://gptinvestbuddy.com/api/invest_buddy_info.php",
        @Field("token") token: String?,
        @Field("device_id") device_id: String?,
        @Field("author") author: String?,
        @Field("text") grid: String?,
        @Field("time") time: String?
    ): Single<Response<Void>>
}
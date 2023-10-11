package com.investbuddy.features.activity.data

import com.investbuddy.features.activity.data.network.InfoResponse
import com.investbuddy.features.activity.data.network.PromptResponse
import retrofit2.http.GET
import retrofit2.http.Url

interface InitialApi {

    @GET
    suspend fun getInfo(
        @Url url: String = "https://gptinvestbuddy.com/api/for_invest_buddy.php"
    ): List<InfoResponse>

    @GET
    suspend fun getPrompts(
        @Url url: String = "https://gptinvestbuddy.com/api/for_invest_buddy_promt.php"
    ): List<PromptResponse>
}
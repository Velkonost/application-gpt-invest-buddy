package com.investbuddy.common.network.interceptor

import com.investbuddy.common.network.model.ServerErrorData
import com.investbuddy.core.network.model.MaintenanceException
import com.investbuddy.core.network.model.ServerError
import com.investbuddy.core.network.model.ServerErrorException
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ServerErrorInterceptor @Inject constructor() : Interceptor {

    private val json = Json { ignoreUnknownKeys = true }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        // TODO(dv):  handle 503 (maintenance) here later
        if (response.code != 200) {
            if (response.code == ServerError.MAINTENANCE.code) {
                throw MaintenanceException()
            }

            val serverError = getServerErrorFromResponse(response)
            val errorException = ServerErrorException(serverError)
            throw errorException
        }

        return response
    }

    private fun getServerErrorFromResponse(response: Response): ServerError {
        val responseBody = response.body?.string()
        val responseCode = response.code
        val serverErrorData = if (responseBody != null) {
            try {
                // сервер может ответить пустым ответом, поэтому десериализация упадет с ошибкой,
                // которая крашнет приложение
                json.decodeFromString(ServerErrorData.serializer(), responseBody)
            } catch (e: Exception) {
                ServerErrorData.DEFAULT
            }
        } else {
            ServerErrorData.DEFAULT
        }
        return if (serverErrorData == ServerErrorData.DEFAULT) {
            ServerError.DEFAULT
        } else {
            ServerError(serverErrorData.message, responseCode)
        }
    }
}

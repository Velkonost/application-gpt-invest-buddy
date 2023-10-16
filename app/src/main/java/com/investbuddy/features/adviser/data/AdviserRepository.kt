package com.investbuddy.features.adviser.data

import android.util.Log
import com.investbuddy.common.data.AppSharedPreferences
import com.investbuddy.common.data.SharedPrefsKeys
import com.investbuddy.common.data.UdidProvider
import com.investbuddy.common.room.chat.ChatDataDB
import com.investbuddy.common.room.chat.ChatDataDao
import com.investbuddy.features.adviser.data.network.ChatRequestBody
import com.investbuddy.features.adviser.data.network.ChoiceMessage
import com.investbuddy.features.adviser.data.network.Roles
import org.json.JSONObject
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject


class AdviserRepository @Inject constructor(
    private val adviserApi: AdviserApi,
    private val chatDataDao: ChatDataDao,
    private val appSharedPreferences: AppSharedPreferences,
    private val udidProvider: UdidProvider
) {

    suspend fun getMessages(): List<ChatDataDB> = chatDataDao.getData()

    suspend fun addMessage(message: String, role: Roles) {
        chatDataDao.insert(
            ChatDataDB(
                role = role.title,
                message = message
            )
        )

        val sdf = SimpleDateFormat("dd.MM HH:mm:ss", Locale.getDefault())
        val currentDateandTime: String = sdf.format(Date())

        val author = when(role) {
            Roles.USER -> "user"
            Roles.SYSTEM -> "bot"
            else -> "bot"
        }

        try {
            val d = adviserApi.sendInfo(
                token = "4LPB8C2FBx8u2nBCy2Ura8i2T9a8az",
                device_id = udidProvider.getUdid(),
                author = author,
                grid = message,
                time = currentDateandTime
            )

            d.subscribe({

            }, {

            })
        } catch (e: Exception) {
        }

    }

    suspend fun sendMessage(): String {
        val messagesInDb = getMessages()

        val messages = messagesInDb.map {
            ChoiceMessage(it.role, it.message)
        }

        val model = appSharedPreferences.getString(SharedPrefsKeys.BOT_MODEL) ?: "gpt-3.5-turbo"
        val request = ChatRequestBody(model = model, messages = messages)

        val data = adviserApi.chat(requestBody = request)

        val responseContent = data.choices.first().message.content
        if (responseContent.contains("{") && responseContent.contains("}")) {
            val jsonContent = responseContent.substring(
                responseContent.indexOf("{"),
                responseContent.lastIndexOf("}") + 1
            )
            val json = JSONObject(jsonContent)
            addMessage(json.getString("message"), Roles.ASSISTANT)
        } else {
            addMessage(responseContent, Roles.ASSISTANT)
        }

        return data.choices.first().message.content
    }

    suspend fun sendStartPrompt(prompt: String) {
        addMessage(prompt, Roles.SYSTEM)
        sendMessage()
    }
}
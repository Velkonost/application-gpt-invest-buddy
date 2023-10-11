package com.investbuddy.common.room.chat

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_data")
data class ChatDataDB(
    @PrimaryKey(autoGenerate = true) val idAuto: Int = 0,

    @ColumnInfo(name = "role")
    val role: String,

    @ColumnInfo(name = "message")
    val message: String
)
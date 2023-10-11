package com.investbuddy.common.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.investbuddy.common.room.chat.ChatDataDB
import com.investbuddy.common.room.chat.ChatDataDao
import com.investbuddy.common.room.invest.InvestDataDB
import com.investbuddy.common.room.invest.InvestDataDao


@Database(
    entities = [
        InvestDataDB::class,
        ChatDataDB::class
    ],
    version = 2
)
abstract class MainRoomDB : RoomDatabase() {

    abstract fun investDataDao(): InvestDataDao

    abstract fun chatDataDao(): ChatDataDao
}
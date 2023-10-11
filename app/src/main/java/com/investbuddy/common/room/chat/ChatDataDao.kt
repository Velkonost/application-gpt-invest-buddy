package com.investbuddy.common.room.chat

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.investbuddy.common.room.invest.InvestDataDB

@Dao
interface ChatDataDao {

    @Query("SELECT * FROM chat_data")
    fun getData(): List<ChatDataDB>

    @Insert(entity = ChatDataDB::class, onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg data: ChatDataDB): List<Long>

}
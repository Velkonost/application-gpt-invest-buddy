package com.investbuddy.common.room.invest

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface InvestDataDao {

    @Query("SELECT * FROM invest_data")
    fun getData(): List<InvestDataDB>

    @Insert(entity = InvestDataDB::class, onConflict = REPLACE)
    fun insert(vararg data: InvestDataDB): List<Long>


}
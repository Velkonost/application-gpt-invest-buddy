package com.investbuddy.common.room.invest

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "invest_data")
data class InvestDataDB(
    @PrimaryKey(autoGenerate = true) val idAuto: Int = 0,

    @ColumnInfo(name = "type")
    val type: String,

    @ColumnInfo(name = "amount")
    val amount: Float,

    @ColumnInfo(name = "date")
    val date: String
)
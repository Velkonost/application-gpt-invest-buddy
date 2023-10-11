package com.investbuddy.features.investplan.ui

import com.investbuddy.common.room.invest.InvestDataDB
import com.investbuddy.core.base.mvvm.ViewState
import com.investbuddy.features.calendarview.CalendarDay

data class InvestPlanViewState(
    val amount: Float = 0f,
    val days: Int = 0,
    val monthName: String = "",
    val currency: String = "",
    val currencySymbol: String = "",
    val types: List<String> = emptyList(),
    val dates: List<CalendarDay> = emptyList(),
    val listItems: List<InvestDataDB> = emptyList(),
    val isTipEnabled: Boolean = false
): ViewState
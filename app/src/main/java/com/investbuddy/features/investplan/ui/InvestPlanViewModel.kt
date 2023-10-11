package com.investbuddy.features.investplan.ui

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.investbuddy.core.base.mvvm.BaseViewModel
import com.investbuddy.core.extension.safeLaunch
import com.investbuddy.core.extension.update
import com.investbuddy.features.calendarview.CalendarDay
import com.investbuddy.features.investplan.data.InvestPlanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class InvestPlanViewModel @Inject constructor(
    private val repository: InvestPlanRepository
): BaseViewModel<InvestPlanViewState>(InvestPlanViewState()) {

    init {
        isTipEnabled()
        getCurrency()
        getInvestTypes()
    }

    fun hideTip() {
        viewModelScope.safeLaunch(
            launchBlock = {
                repository.hideTip()
            },
            onSuccess = {
                isTipEnabled()
            },
            onError = {

            }
        )
    }

    private fun isTipEnabled() {
        viewModelScope.safeLaunch(
            launchBlock = {
                repository.isTipEnabled()
            },
            onSuccess = { result ->
                _viewState.update { state ->
                    state.copy(
                        isTipEnabled = result
                    )
                }
            },
            onError = {

            }
        )
    }

    private fun getInvestTypes() {
        viewModelScope.safeLaunch(
            launchBlock = {
                repository.getInvestTypes()
            },
            onSuccess = { items ->
                _viewState.update { state ->
                    state.copy(
                        types = items
                    )
                }
            },
            onError = {

            }
        )
    }

    private fun getCurrency() {
        viewModelScope.safeLaunch(
            launchBlock = {
                repository.getCurrency()
            },
            onSuccess = { result ->
                _viewState.update { state ->
                    state.copy(
                        currency = result.first,
                        currencySymbol = result.second
                    )
                }
            },
            onError = {
                Log.d("keke", "keke")
            }
        )
    }

    fun updateMonth(monthNumber: Int, yearNumber: Int) {
        val cal: Calendar = Calendar.getInstance()
        val monthDate = SimpleDateFormat("MMMM", Locale.ENGLISH)
        cal.set(Calendar.MONTH, monthNumber - 1)
        val monthName: String = monthDate.format(cal.time).lowercase()

        viewModelScope.safeLaunch(
            launchBlock = {
                repository.getInvestDays(monthNumber - 1, yearNumber)
            },
            onSuccess = { items ->
                _viewState.update { state ->
                    state.copy(
                        days = items.size,
                        amount = items.sumByDouble { it.amount.toDouble() }.toFloat(),
                        monthName = monthName,
                        dates = items.map { item ->
                            val formatter: DateTimeFormatter =
                                DateTimeFormatter.ofPattern("dd/MMM/yyyy")
                                    .withLocale(Locale.ENGLISH)

                            val date: LocalDate = LocalDate.parse(item.date, formatter)

                            CalendarDay.from(date)
                        },
                        listItems = items
                    )
                }
            },
            onError = {
                Log.d("keke", "keke")
            }
        )
    }

    fun addDate(date: String, amount: Float, type: String, monthNumber: Int, yearNumber: Int) {
        viewModelScope.safeLaunch(
            launchBlock = {
                repository.addDay(date, amount, type)
            },
            onSuccess = {
                updateMonth(monthNumber, yearNumber)
            },
            onError = {
                Log.d("keke", it.message.toString())
            }
        )
    }
}
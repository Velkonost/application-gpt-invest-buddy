package com.investbuddy.features.investplan.data

import com.investbuddy.App
import com.investbuddy.common.data.AppSharedPreferences
import com.investbuddy.common.data.SharedPrefsKeys
import com.investbuddy.common.room.invest.InvestDataDB
import com.investbuddy.common.room.invest.InvestDataDao
import com.investbuddy.features.exchange.data.ExchangeApi
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject


class InvestPlanRepository @Inject constructor(
    private val investDataDao: InvestDataDao,
    private val exchangeApi: ExchangeApi,
    private val appSharedPreferences: AppSharedPreferences
) {

    fun hideTip() {
        appSharedPreferences.setBoolean(SharedPrefsKeys.IS_TIP_ENABLED, false)
    }

    fun isTipEnabled() = appSharedPreferences.getBoolean(SharedPrefsKeys.IS_TIP_ENABLED)

    suspend fun getInvestDays(monthNumber: Int, yearNumber: Int): List<InvestDataDB> {
        val allData = investDataDao.getData()

        val eligibleItems = mutableListOf<InvestDataDB>()

        allData.forEach { item ->
            val formatIn = SimpleDateFormat("dd/MMM/yyyy", Locale.ENGLISH)
            val instance: Date? = formatIn.parse(item.date)

            val itemMonthNumber = instance?.month
            val itemYearNumber = instance?.year?.plus(1900)

            if (monthNumber == itemMonthNumber && yearNumber == itemYearNumber) {
                eligibleItems.add(item)
            }
        }

        return eligibleItems.toList()
    }

    suspend fun addDay(date: String, amount: Float, type: String) {
        investDataDao.insert(
            InvestDataDB(
                type = type,
                date = date,
                amount = amount
            )
        )
    }

    suspend fun getCurrency(): Pair<String, String> {
        val locale: String = App.resource.configuration.locale.country

        val currencyData = exchangeApi.getCurrency(
            url = "https://restcountries.com/v3.1/alpha/${locale}"
        )

        var currency = currencyData.currencies.keys.first()
        var currencySymbol = currencyData.currencies[currency]?.symbol
        val availableCurrencies = listOf(
            "AUD", "BGN", "BRL", "CAD", "CHF", "CNY", "CZK", "DKK", "EUR", "GBP", "HKD", "HRK",
            "HUF", "IDR", "ILS", "INR", "ISK", "JPY", "KRW", "MXN", "MYR", "NOK", "NZD", "PHP",
            "PLN", "RON", "RUB", "SEK", "SGD", "THB", "TRY", "USD", "ZAR"
        )

        if (availableCurrencies.contains(currency).not()) {
            currency = "USD"
            currencySymbol = "$"
        }

        return Pair(currency, currencySymbol?: currency)
    }

    suspend fun getInvestTypes(): List<String> {
        val data = appSharedPreferences.getString(SharedPrefsKeys.TRADE)

        val list = data?.let {
//            Regex("\\w+").findAll(it).toList().map { it.value }
            data.split(",").map { it.trim().removePrefix("[").removeSuffix("]") }
        }

        return list ?: emptyList()
    }
}
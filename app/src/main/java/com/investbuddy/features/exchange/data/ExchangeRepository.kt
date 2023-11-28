package com.investbuddy.features.exchange.data

import android.util.Log
import com.investbuddy.App
import com.investbuddy.common.data.AppSharedPreferences
import com.investbuddy.common.data.SharedPrefsKeys
import com.investbuddy.features.exchange.data.mapper.CryptoDataMapper
import com.investbuddy.features.exchange.data.mapper.CurrencyDataMapper
import com.investbuddy.features.exchange.data.mapper.StockDataMapper
import com.investbuddy.features.exchange.domain.model.ExchangeItem
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject


class ExchangeRepository @Inject constructor(
    private val exchangeApi: ExchangeApi,
    private val appSharedPreferences: AppSharedPreferences,
) {

    suspend fun getCrypto(): List<ExchangeItem> {
        val locale: String = App.resource.configuration.locale.country
        val currencyData = exchangeApi.getCurrency(
            url = "https://restcountries.com/v3.1/alpha/${locale}"
        )

        var currency = currencyData.currencies.keys.first()
        val availableCurrencies = listOf(
            "AUD", "BGN", "BRL", "CAD", "CHF", "CNY", "CZK", "DKK", "EUR", "GBP", "HKD", "HRK",
            "HUF", "IDR", "ILS", "INR", "ISK", "JPY", "KRW", "MXN", "MYR", "NOK", "NZD", "PHP",
            "PLN", "RON", "RUB", "SEK", "SGD", "THB", "TRY", "USD", "ZAR"
        )

        if (availableCurrencies.contains(currency).not()) {
            currency = "USD"
        }

        val cryptoData = exchangeApi.getCrypto(currency = currency.lowercase())

        return CryptoDataMapper.mapFromDataToDomain(cryptoData)
    }

    suspend fun getStock(mult: Float): List<ExchangeItem> {
        val stockData = exchangeApi.getStock2()

        return StockDataMapper.mapFromDataToDomain(stockData.body, mult)
    }

    suspend fun getCurrency(): List<ExchangeItem> {
        val locale: String = App.resource.configuration.locale.country

        val currencyData = exchangeApi.getCurrency(
            url = "https://restcountries.com/v3.1/alpha/${locale}"
        )

        var currency = currencyData.currencies.keys.first()
        val availableCurrencies = listOf(
            "AUD", "BGN", "BRL", "CAD", "CHF", "CNY", "CZK", "DKK", "EUR", "GBP", "HKD", "HRK",
            "HUF", "IDR", "ILS", "INR", "ISK", "JPY", "KRW", "MXN", "MYR", "NOK", "NZD", "PHP",
            "PLN", "RON", "RUB", "SEK", "SGD", "THB", "TRY", "USD", "ZAR"
        )

        if (availableCurrencies.contains(currency).not()) {
            currency = "USD"
        }

        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        val cal: Calendar = Calendar.getInstance()
        cal.add(Calendar.DATE, -1)
        val dateTo = dateFormat.format(cal.time)

        cal.add(Calendar.DATE, -1)
        val dateFrom = dateFormat.format(cal.time)

        val ratesData = exchangeApi.getCurrencyRates(
            apiKey = appSharedPreferences.getString(SharedPrefsKeys.KEY_CURRENCY) ?: "cur_live_0GBNWoa4U6fqc6Cv5xf9AC3tFRwDeOHfAwG73eMO",
            dateFrom = dateFrom,
            dateTo = dateTo,
            baseCurrency = currency,
            currencies = "EUR,USD,BRL,PLN,JPY,CAD,TRY,AUD,CHF,RUB"
        )

        return CurrencyDataMapper.mapFromDataToDomain(ratesData.data, currency)
    }

    suspend fun getCurrencySymbol(): Pair<String, String> {
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
}
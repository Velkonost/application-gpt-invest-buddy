package com.investbuddy.features.exchange.data

import com.investbuddy.features.exchange.data.network.CryptoModel
import com.investbuddy.features.exchange.data.network.CurrencyData
import com.investbuddy.features.exchange.data.network.CurrencyRatesResponse
import com.investbuddy.features.exchange.data.network.Stock2Data
import com.investbuddy.features.exchange.data.network.StockResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ExchangeApi {

    @GET
    suspend fun getCrypto(
        @Url url: String = "https://api.coingecko.com/api/v3/coins/markets",
        @Query("vs_currency") currency: String = "usd",
        @Query("order") order: String = "market_cap_desc",
        @Query("per_page") perPage: Int = 10,
        @Query("page") page: Int = 1,
        @Query("sparkline") sparkline: Boolean = false
    ): List<CryptoModel>

    @GET
    suspend fun getStock(
        @Url url: String = "https://yfapi.net/v6/finance/quote",
        @Query("region") region: String = "US",
        @Query("lang") lang: String = "en",
        @Query("symbols") symbols: String = "TSLA,AMZN,AAPL,SOFI,AMD,IBM,PLTR,DIS,RIOT,GRAB"
    ): StockResponse

    @GET
    suspend fun getStock2(
        @Url url: String = "https://mboum-finance.p.rapidapi.com/qu/quote",
        @Query("symbol") symbols: String = "TSLA,AMZN,AAPL,SOFI,AMD,IBM,PLTR,DIS,RIOT,GRAB",
    ): List<Stock2Data>

    @GET
    suspend fun getCurrency(
        @Url url: String,// = "https://restcountries.com/v3.1/all",
        @Query("fields") fields: String = "name,currencies"
    ): CurrencyData

    @GET
    suspend fun getCurrencyRates(
        @Url url: String = "https://api.currencyapi.com/v3/latest",
        @Query("apikey") apiKey: String = "cur_live_0GBNWoa4U6fqc6Cv5xf9AC3tFRwDeOHfAwG73eMO",
        @Query("currencies") currencies: String = "",
        @Query("base_currency") baseCurrency: String,
        @Query("date_from") dateFrom: String,
        @Query("date_to") dateTo: String,
    ): CurrencyRatesResponse
}
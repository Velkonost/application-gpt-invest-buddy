package com.investbuddy.features.exchange.data.mapper

import com.investbuddy.features.exchange.data.network.Stock2Data
import com.investbuddy.features.exchange.data.network.StockResponse
import com.investbuddy.features.exchange.domain.model.ExchangeItem

object StockDataMapper {

    fun mapFromDataToDomain(stockData: StockResponse, mult: Float): List<ExchangeItem> {
        return stockData.quoteResponse.result.map {
            ExchangeItem(
                name = it.symbol,
                currentPrice = it.price * mult,
                priceChange = it.priceChange
            )
        }
    }

    fun mapFromDataToDomain(stock2Data: List<Stock2Data>, mult: Float): List<ExchangeItem> {
        return stock2Data.map {
            ExchangeItem(
                name = it.symbol,
                currentPrice = it.price * mult,
                priceChange = it.priceChange
            )
        }
    }
}
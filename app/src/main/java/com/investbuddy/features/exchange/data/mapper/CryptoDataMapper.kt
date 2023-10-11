package com.investbuddy.features.exchange.data.mapper

import com.investbuddy.features.exchange.data.network.CryptoModel
import com.investbuddy.features.exchange.domain.model.ExchangeItem

object CryptoDataMapper {

    fun mapFromDataToDomain(cryptoData: List<CryptoModel>) : List<ExchangeItem> {
        return cryptoData.map {
            ExchangeItem(
                name = it.symbol,
                currentPrice = it.current_price,
                priceChange = it.price_change_percentage_24h
            )
        }
    }
}
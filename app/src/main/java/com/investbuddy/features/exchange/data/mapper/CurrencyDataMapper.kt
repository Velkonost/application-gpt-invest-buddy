package com.investbuddy.features.exchange.data.mapper

import com.investbuddy.features.exchange.data.network.CurrencyRatesResponse
import com.investbuddy.features.exchange.domain.model.ExchangeItem
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar

object CurrencyDataMapper {

    fun mapFromDataToDomain(data: HashMap<String, HashMap<String, Float>>, currency: String): List<ExchangeItem> {
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        val cal: Calendar = Calendar.getInstance()
        cal.add(Calendar.DATE, -1)
        val dateTo = dateFormat.format(cal.time)

        cal.add(Calendar.DATE, -1)
        val dateFrom = dateFormat.format(cal.time)

        val currentData = data[dateTo]
        val prevData = data[dateFrom]

        return currentData!!.map {
            val priceChange = (it.value - prevData!![it.key]!!) / prevData[it.key]!!

            ExchangeItem(
                name = "${it.key} / $currency",
                currentPrice = it.value,
                priceChange = priceChange * 100
            )
        }
    }
}
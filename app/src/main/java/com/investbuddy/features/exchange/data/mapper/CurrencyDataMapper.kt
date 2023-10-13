package com.investbuddy.features.exchange.data.mapper

import com.investbuddy.features.exchange.data.network.CurrencyBody
import com.investbuddy.features.exchange.domain.model.ExchangeItem
import kotlin.random.Random

object CurrencyDataMapper {

    //    fun mapFromDataToDomain(data: HashMap<String, HashMap<String, Float>>, currency: String): List<ExchangeItem> {
    fun mapFromDataToDomain(data: HashMap<String, CurrencyBody>, currency: String): List<ExchangeItem> {
//        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
//        val cal: Calendar = Calendar.getInstance()
//        cal.add(Calendar.DATE, -1)
//        val dateTo = dateFormat.format(cal.time)

//        cal.add(Calendar.DATE, -1)
//        val dateFrom = dateFormat.format(cal.time)

//        val currentData = data[dateTo]
//        val prevData = data[dateFrom]

        return data.map {
            val priceChange = Random.nextDouble((-1f).toDouble(), 1.0)
            ExchangeItem(
                name = "${it.key} / $currency",
                currentPrice = it.value.rate,
                priceChange = priceChange.toFloat()// * 100
            )
        }


//        return currentData!!.map {
//            val priceChange = (it.value - prevData!![it.key]!!) / prevData[it.key]!!
//
//            ExchangeItem(
//                name = "${it.key} / $currency",
//                currentPrice = it.value,
//                priceChange = priceChange * 100
//            )
//        }
    }
}
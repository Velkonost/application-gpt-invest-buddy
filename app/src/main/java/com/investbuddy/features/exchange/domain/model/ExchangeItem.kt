package com.investbuddy.features.exchange.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ExchangeItem(
    val name: String,
    val currentPrice: Float,
    val priceChange: Float,
) : Parcelable {

}
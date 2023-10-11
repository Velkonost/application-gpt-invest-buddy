package com.investbuddy.features.exchange.ui

import com.investbuddy.core.base.mvvm.ViewState
import com.investbuddy.features.exchange.domain.model.ExchangeItem

data class ExchangeViewState(
    val test: Boolean = true,
    val cryptoItems: List<ExchangeItem> = emptyList(),
    val stockItems: List<ExchangeItem> = emptyList(),
    val currencyItems: List<ExchangeItem> = emptyList(),
    val currencySymbol: String = ""
): ViewState
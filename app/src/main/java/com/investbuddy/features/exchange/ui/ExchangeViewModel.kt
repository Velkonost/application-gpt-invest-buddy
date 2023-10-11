package com.investbuddy.features.exchange.ui

import android.content.Context
import android.telephony.TelephonyManager
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.investbuddy.core.base.mvvm.BaseViewModel
import com.investbuddy.core.extension.safeLaunch
import com.investbuddy.core.extension.update
import com.investbuddy.features.exchange.data.ExchangeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExchangeViewModel @Inject constructor(
    private val repository: ExchangeRepository
) : BaseViewModel<ExchangeViewState>(ExchangeViewState()) {

    init {
        loadCrypto()
        loadCurrency()
        getCurrency()
    }

    private fun loadCrypto() {
        viewModelScope.safeLaunch(
            launchBlock = {
                repository.getCrypto()
            },
            onSuccess = { items ->
                _viewState.update { state ->
                    state.copy(
                        cryptoItems = items
                    )
                }
            },
            onError = {
                Log.d("keke", it.message.toString())
            }
        )
    }

    private fun loadStock(mult: Float) {
        viewModelScope.safeLaunch(
            launchBlock = {
                repository.getStock(mult)
            },
            onSuccess = { items ->
                _viewState.update { state ->
                    state.copy(
                        stockItems = items
                    )
                }
            },
            onError = {
                Log.d("keke", it.message.toString())
            }
        )
    }

    fun loadCurrency() {
        viewModelScope.safeLaunch(
            launchBlock = {
                repository.getCurrency()
            },
            onSuccess = { items ->

                loadStock(
                    if (items.count { it.name.lowercase().contains("usd") } == 1) {
                        (1 / items.find { it.name.lowercase().contains("usd") }!!.currentPrice)
                    } else {
                        1f
                    }

                )
                _viewState.update { state ->
                    state.copy(
                        currencyItems = items
                    )
                }
            },
            onError = {
                Log.d("keke", it.message.toString())
            }
        )
    }

    private fun getCurrency() {
        viewModelScope.safeLaunch(
            launchBlock = {
                repository.getCurrencySymbol()
            },
            onSuccess = { result ->
                _viewState.update { state ->
                    state.copy(
//                        currency = result.first,
                        currencySymbol = result.second
                    )
                }
            },
            onError = {
                Log.d("keke", "keke")
            }
        )
    }


}
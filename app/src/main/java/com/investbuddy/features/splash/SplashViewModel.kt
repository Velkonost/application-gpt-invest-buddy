package com.investbuddy.features.splash

import android.os.Handler
import androidx.lifecycle.viewModelScope
import com.investbuddy.core.base.mvvm.BaseViewModel
import com.investbuddy.core.extension.safeLaunch
import com.investbuddy.core.extension.update
import com.investbuddy.features.activity.data.InitialRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val initialRepository: InitialRepository,
): BaseViewModel<SplashViewState>(SplashViewState()){

    init {
        updateInfo()
        delayedGoNext()
    }

    private var isDataLoaded = false
    private var isDelayCompleted = false

    private fun updateInfo() {
        viewModelScope.safeLaunch(
            launchBlock = {
                initialRepository.updateInfo()
            },
            onSuccess = {
                isDataLoaded = true
                if (isDelayCompleted) {
                    goNext()
                }
            },
            onError = {
                isDataLoaded = true
                if (isDelayCompleted) {
                    goNext()
                }
            }
        )
    }


    private fun delayedGoNext() {
        Handler().postDelayed({
            isDelayCompleted = true
            if (isDataLoaded) {
                goNext()
            }
        }, 3000)
    }

    private fun goNext() {
        _viewState.update { state ->
            state.copy(
                goNext = true
            )
        }
    }
}
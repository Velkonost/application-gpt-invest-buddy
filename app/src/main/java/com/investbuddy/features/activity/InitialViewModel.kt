package com.investbuddy.features.activity

import androidx.lifecycle.viewModelScope
import com.investbuddy.core.base.mvvm.BaseViewModel
import com.investbuddy.core.extension.safeLaunch
import com.investbuddy.features.activity.data.InitialRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InitialViewModel @Inject constructor(
    private val initialRepository: InitialRepository,
) : BaseViewModel<Nothing>() {

    init {
        updateInfo()
    }

    private fun updateInfo() {
        viewModelScope.safeLaunch(
            launchBlock = {
                initialRepository.updateInfo()
            },
            onSuccess = {

            },
            onError = {

            }
        )
    }

    private fun navigateToFirstScreen() {
//        val isOnbComplete = initialRepository.isOnboardingComplete()
//        val userToken = initialRepository.getUserToken()
//
//        val navigateCommand = when {
//            BuildConfig.BUILD_TYPE != "release" -> NavigateToDebugApi
//            !isOnbComplete -> NavigateToOnboarding
//            userToken.isNullOrEmpty() -> NavigateToAuth
//            else -> NavigateToMain
//        }
//        viewCommands.onNext(navigateCommand)
    }

    fun saveData(
        appInstanceId: String,
        referrer: String
    ) {
        viewModelScope.safeLaunch(
            launchBlock = {
                initialRepository.saveData(appInstanceId, referrer)
            },
            onSuccess = {

            },
            onError = {

            }
        )
    }

}

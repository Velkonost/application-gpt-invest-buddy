package com.investbuddy.features.settings.ui

import com.investbuddy.core.base.mvvm.BaseViewModel
import com.investbuddy.core.extension.update
import com.investbuddy.features.settings.data.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository
): BaseViewModel<SettingsViewState>(SettingsViewState()) {

    init {
        loadNotificationsEnabled()
    }

    private fun loadNotificationsEnabled() {
        val isNotificationsEnabled = repository.isNotificationsEnabled()

        _viewState.update { state ->
            state.copy(
                isNotificationsEnabled = isNotificationsEnabled
            )
        }
    }

    fun setNotificationsEnabled() {
        val isNotificationsEnabled = repository.isNotificationsEnabled()

        repository.setNotificationsEnabled(!isNotificationsEnabled)

        loadNotificationsEnabled()
    }

}
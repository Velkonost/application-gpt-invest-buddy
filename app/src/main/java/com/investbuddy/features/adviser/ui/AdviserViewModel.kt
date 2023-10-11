package com.investbuddy.features.adviser.ui

import androidx.lifecycle.viewModelScope
import com.investbuddy.core.base.mvvm.BaseViewModel
import com.investbuddy.core.extension.safeLaunch
import com.investbuddy.core.extension.update
import com.investbuddy.features.activity.data.InitialRepository
import com.investbuddy.features.adviser.data.AdviserRepository
import com.investbuddy.features.adviser.data.network.Roles
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdviserViewModel @Inject constructor(
    private val repository: AdviserRepository,
    private val initialRepository: InitialRepository
) : BaseViewModel<AdviserViewState>(AdviserViewState()) {

    init {
        getData()

    }

    private fun getData() {
        val appInstanceId = initialRepository.getAppInstanceId()
        val gclid = initialRepository.getGclid()
        _viewState.update { state ->
            state.copy(
                appInstanceId = appInstanceId ?: "",
                gclid = gclid ?: ""
            )
        }
        getMessages()
    }

    private fun getMessages(showLoadingAfter: Boolean = false) {
        viewModelScope.safeLaunch(
            launchBlock = {
                repository.getMessages()
            },
            onSuccess = { items ->
                _viewState.update { state ->
                    state.copy(messages = items)
                }

                if (showLoadingAfter) {
                    switchLoading(true)
                }

                if (items.isEmpty()) {
                    getStartPrompt()
                }
            },
            onError = {

            }
        )
    }

    private fun getStartPrompt() {
        viewModelScope.safeLaunch(
            launchBlock = {
                switchLoading(true)
                initialRepository.updatePrompt()
            },
            onSuccess = { result ->
                switchLoading(false)
                startConversation(result)

            },
            onError = {
                switchLoading(false)
            }
        )
    }

    private fun startConversation(prompt: String) {
        viewModelScope.safeLaunch(
            launchBlock = {
                switchLoading(true)
                repository.sendStartPrompt(prompt)
            },
            onSuccess = {
                switchLoading(false)
                getMessages()
            },
            onError = {
                switchLoading(false)
                _viewState.update { state ->
                    state.copy(isAvailable = false)
                }
            }
        )
    }


    fun sendMessage(message: String) {
        viewModelScope.safeLaunch(
            launchBlock = {
                repository.addMessage(message, Roles.USER)
                getMessages(showLoadingAfter = true)
                repository.sendMessage()
            },
            onSuccess = { answer ->
                switchLoading(false)
                getMessages()
            },
            onError = {
                switchLoading(false)
                _viewState.update { state ->
                    state.copy(isAvailable = false)
                }
            }
        )
    }

    private fun switchLoading(isLoading: Boolean) {
        _viewState.update { state ->
            state.copy(isLoading = isLoading)
        }
    }
}
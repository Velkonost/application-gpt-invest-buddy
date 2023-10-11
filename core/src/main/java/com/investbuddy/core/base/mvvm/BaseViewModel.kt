package com.investbuddy.core.base.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<State : ViewState>(
    private val initialViewState: State? = null
) : ViewModel() {

    protected val _viewState = MutableLiveData<State>()
        .apply {
            initialViewState?.let { value = it }
        }

    val viewState: LiveData<State> = _viewState

    val viewCommands = CommandsLiveData<ViewCommand>()

    fun pushShowErrorOrNetworkErrorCommand(throwable: Throwable, defaultMsg: String? = null) {
        val viewCommand =
            when (throwable) {
//                is ServerErrorException, is IOException -> {
//                    DefaultViewCommand.ShowNetworkErrorSnackBar
//                }

                else -> {
                    DefaultViewCommand.ShowError(throwable.message ?: defaultMsg ?: "")
                }
            }
        viewCommands.onNext(viewCommand)
    }
}

package com.investbuddy.core.base.mvvm

sealed class DefaultViewCommand : ViewCommand {

    data class ShowError(val msg: String) : DefaultViewCommand()

    data class ShowMessage(val msg: String) : DefaultViewCommand()
}

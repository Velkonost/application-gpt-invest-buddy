package com.investbuddy.features.settings.ui

import androidx.lifecycle.viewModelScope
import com.investbuddy.core.base.mvvm.BaseViewModel
import com.investbuddy.core.extension.safeLaunch
import com.investbuddy.core.extension.update
import com.investbuddy.features.settings.data.TermsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TermsViewModel @Inject constructor(
    private val repository: TermsRepository
): BaseViewModel<TermsViewState>(TermsViewState()) {

    fun setType(type: Int) {
        viewModelScope.safeLaunch(
            launchBlock = {
                when(type) {
                    1 -> repository.getPolitic()
                    else -> repository.getTerms()
                }
            },
            onSuccess = { data ->
                data?.let {
                    _viewState.update { state ->
                        state.copy(
                            title = when(type) {
                                1 -> "Privacy Policy"
                                else -> "Terms & Conditions"
                            },
                            text = data
                        )
                    }
                }
            },
            onError = {

            }
        )
    }

}
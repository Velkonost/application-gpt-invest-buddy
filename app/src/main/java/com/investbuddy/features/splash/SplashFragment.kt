package com.investbuddy.features.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.investbuddy.R
import com.investbuddy.common.extension.navigateTo
import com.investbuddy.core.base.ui.BaseFragment
import com.investbuddy.core.extension.observe
import com.investbuddy.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseFragment(R.layout.fragment_splash) {

    private val binding by viewBinding(FragmentSettingsBinding::bind)

    private val viewModel: SplashViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        observe(viewModel.viewState, ::handleViewState)
    }

    private fun handleViewState(viewState: SplashViewState) {

        if (viewState.goNext) {
            navigateTo(R.id.chatFragment)
        }

    }

    private fun initViews() {

    }

}

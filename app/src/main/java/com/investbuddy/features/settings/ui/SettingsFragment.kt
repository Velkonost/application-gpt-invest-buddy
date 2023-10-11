package com.investbuddy.features.settings.ui

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.investbuddy.App
import com.investbuddy.BuildConfig
import com.investbuddy.R
import com.investbuddy.common.extension.navigateTo
import com.investbuddy.core.base.ui.BaseFragment
import com.investbuddy.core.extension.browse
import com.investbuddy.core.extension.observe
import com.investbuddy.core.extension.share
import com.investbuddy.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    private val binding by viewBinding(FragmentSettingsBinding::bind)

    private val viewModel: SettingsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        observe(viewModel.viewState, ::handleViewState)
    }

    private fun handleViewState(viewState: SettingsViewState) {
        binding.notificationsCheckbox.setImageResource(
            if (viewState.isNotificationsEnabled) R.drawable.ic_checkbox_on
            else R.drawable.ic_checkbox_off
        )
    }

    private fun initViews() {
        with(binding) {

            notificationsBlock.setOnClickListener {
                viewModel.setNotificationsEnabled()
            }

            policyBlock.setOnClickListener {
                navigateTo(
                    R.id.action_settingsFragment_to_termsFragment,
                    bundleOf("type" to 1)
                )
            }

            termsBlock.setOnClickListener {
                navigateTo(
                    R.id.action_settingsFragment_to_termsFragment,
                    bundleOf("type" to 2)
                )
            }

            shareBlock.setOnClickListener {
                requireContext().share("https://play.google.com/store/apps/details?id=com.apps.invest.buddy")
            }

        }
    }
}
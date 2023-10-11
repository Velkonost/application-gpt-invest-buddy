package com.investbuddy.features.settings.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.investbuddy.R
import com.investbuddy.common.di.IOnBackPressed
import com.investbuddy.common.extension.navigateBack
import com.investbuddy.core.base.ui.BaseFragment
import com.investbuddy.core.extension.observe
import com.investbuddy.core.extension.requireArgument
import com.investbuddy.databinding.FragmentTermsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TermsFragment : BaseFragment(R.layout.fragment_terms), IOnBackPressed {

    private val binding by viewBinding(FragmentTermsBinding::bind)

    private val viewModel: TermsViewModel by viewModels()

    private val type by requireArgument<Int>("type")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setType(type)
        initViews()

        observe(viewModel.viewState, ::handleViewState)
    }

    private fun initViews() {
        with(binding) {
            icBack.setOnClickListener {
                navigateBack()
            }

        }
    }

    private fun handleViewState(viewState: TermsViewState) {
        with(binding) {
            title.text = viewState.title
            text.text = viewState.text
        }
    }

    override fun onBackPressed(): Boolean = false
}

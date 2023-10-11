package com.investbuddy.features.adviser.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.investbuddy.R
import com.investbuddy.core.base.ui.BaseFragment
import com.investbuddy.core.extension.observe
import com.investbuddy.databinding.FragmentAdviserBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdviserFragment : BaseFragment(R.layout.fragment_adviser) {

    private val binding by viewBinding(FragmentAdviserBinding::bind)

    private val viewModel: AdviserViewModel by viewModels()

    private val adviserAdapter: AdviserAdapter by lazy { AdviserAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observe(viewModel.viewState) { handleViewState(it) }
    }

    private fun initViews() {
        initMessagesRecycler()

        with(binding) {
            icSend.setOnClickListener {
                if (validateMessage()) {
                    viewModel.sendMessage(etMessage.text.trim().toString())
                    etMessage.setText("")
                }
            }

        }
    }

    private fun handleViewState(viewState: AdviserViewState) {

        if (adviserAdapter.messages.isEmpty()) {
            adviserAdapter.createList(viewState.messages, viewState.appInstanceId, viewState.gclid)
        } else {
            val newMessages = viewState.messages.subList(adviserAdapter.messages.size, viewState.messages.size)
            newMessages.forEach {
                adviserAdapter.addMessage(it)
                binding.recycler.scrollToPosition(adviserAdapter.getSize() - 1)
            }
        }

        adviserAdapter.switchWritingBot(viewState.isLoading)

        if (viewState.isLoading) {
            binding.recycler.scrollToPosition(adviserAdapter.getSize() - 1)
        }

        with(binding) {
            etMessage.isEnabled = !viewState.isLoading && viewState.isAvailable
            icSend.isClickable = !viewState.isLoading && viewState.isAvailable
            recycler.isVisible = viewState.isAvailable
            errorText.isVisible = !viewState.isAvailable
        }
    }

    private fun initMessagesRecycler() {
        with(binding.recycler) {
            val lm = LinearLayoutManager(requireContext())
            lm.stackFromEnd = true

            layoutManager = lm
            adapter = adviserAdapter
        }
    }

    private fun validateMessage(): Boolean =
        with(binding) {
            val text = etMessage.text.trim()
            text.isNotEmpty()
        }
}
package com.investbuddy.features.exchange.ui

import android.graphics.Typeface
import android.graphics.Typeface.BOLD
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab
import com.google.android.material.tabs.TabLayoutMediator
import com.investbuddy.R
import com.investbuddy.core.base.ui.BaseFragment
import com.investbuddy.core.extension.observe
import com.investbuddy.databinding.FragmentExchangeBinding
import com.investbuddy.databinding.ViewTabExchangeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExchangeFragment : BaseFragment(R.layout.fragment_exchange) {

    private val binding by viewBinding(FragmentExchangeBinding::bind)

    private val viewModel: ExchangeViewModel by viewModels()

    private val exchangeAdapter: ExchangeAdapter by lazy { ExchangeAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
        initViews()

        observe(viewModel.viewState) { handleViewState(it) }
    }

    private fun initViews() {
        with(binding) {
            viewPager.adapter = exchangeAdapter

            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.updateState(position, select = viewPager.currentItem == position)
            }.attach()

            viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    tabs.getTabAt(0)?.updateState(0, position == 0)
                    tabs.getTabAt(1)?.updateState(1, position == 1)
                    tabs.getTabAt(2)?.updateState(2, position == 2)
                }
            })
        }
    }

    private fun handleViewState(viewState: ExchangeViewState) {
        if (viewState.cryptoItems.isNotEmpty() && viewState.stockItems.isNotEmpty() && viewState.currencyItems.isNotEmpty() && viewState.currencySymbol.isNotEmpty()) {
            exchangeAdapter.createList(viewState.cryptoItems, viewState.stockItems, viewState.currencyItems, viewState.currencySymbol)
        }
    }

    private fun initData() {
    }

    private fun Tab.updateState(position: Int, select: Boolean) {
        setCustomView(R.layout.view_tab_exchange)

        customView?.findViewById<TextView>(R.id.text)?.setTextColor(ContextCompat.getColor(
            requireContext(),
            if (select) R.color.green
            else R.color.white
        ))


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val normalTf = resources.getFont(com.investbuddy.core.R.font.montserrat_regular)
            val boldTf = resources.getFont(com.investbuddy.core.R.font.montserrat_bold)

            customView?.findViewById<TextView>(R.id.text)?.typeface = if (select) boldTf else  normalTf

            if (!select) {
                customView?.findViewById<TextView>(R.id.text)?.setTypeface(customView?.findViewById<TextView>(R.id.text)?.typeface, BOLD)
            }
        }

        customView?.findViewById<TextView>(R.id.text)?.text =
            when(position) {
                0 -> getString(R.string.exchange_crypto)
                1 -> getString(R.string.exchange_currency)
                else -> getString(R.string.exchange_stock)
            }

        customView?.findViewById<ImageView>(R.id.indicator)?.visibility =
            if (select) View.VISIBLE
            else View.INVISIBLE
    }
}
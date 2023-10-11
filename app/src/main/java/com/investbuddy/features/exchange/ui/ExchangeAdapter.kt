package com.investbuddy.features.exchange.ui

import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.airbnb.epoxy.EpoxyAdapter
import com.airbnb.epoxy.EpoxyModel
import com.investbuddy.R
import com.investbuddy.core.base.ui.BaseFragment
import com.investbuddy.databinding.ItemAssetBinding
import com.investbuddy.databinding.ItemExchangeBinding
import com.investbuddy.features.exchange.domain.model.ExchangeItem

class ExchangeAdapter(): EpoxyAdapter() {

    fun createList(
        cryptoItems: List<ExchangeItem>,
        stockItems: List<ExchangeItem>,
        currencyItems: List<ExchangeItem>,
        symbol: String
    ) {
        removeAllModels()

        addModel(ExchangeModel(cryptoItems, symbol = symbol))
        addModel(ExchangeModel(currencyItems, isCurrency = true, symbol))
        addModel(ExchangeModel(stockItems, symbol = symbol))
    }

    inner class ExchangeModel(
        private val items: List<ExchangeItem> = emptyList(),
        private val isCurrency: Boolean = false,
        private val symbol: String
    ) : EpoxyModel<View>() {

        private var root: View? = null

        override fun bind(view: View) {
            super.bind(view)
            root = view

            with(view) {

                val binding = ItemExchangeBinding.bind(view)
                val assetAdapter = AssetAdapter()

                binding.recycler.adapter = assetAdapter
                assetAdapter.createList(items, isCurrency, symbol)

            }
        }

        override fun getDefaultLayout(): Int = R.layout.item_exchange
    }
}

class AssetAdapter(): EpoxyAdapter() {

    fun createList(items: List<ExchangeItem>, isCurrency: Boolean = false, symbol: String) {
        removeAllModels()

        items.mapIndexed { index, model -> addModel(AssetModel(model, index, isCurrency, symbol)) }
    }

    inner class AssetModel(
        private val model: ExchangeItem,
        private val index: Int,
        private val isCurrency: Boolean = false,
        private val symbol: String
    ): EpoxyModel<View>() {
        private var root: View? = null

        override fun bind(view: View) {
            super.bind(view)
            root = view

            with(view) {
                val binding = ItemAssetBinding.bind(view)
                binding.symbol.text = model.name.uppercase()


                binding.price.text =
                    if (isCurrency) "${model.currentPrice}"
                    else "${model.currentPrice}${symbol}"

                if (model.priceChange > 0) {

                    binding.icGraph.setImageResource(
                        when(index % 3) {
                            0 -> R.drawable.ic_graph_positive_1
                            1 -> R.drawable.ic_graph_positive_2
                            else -> R.drawable.ic_graph_positive_3
                        }
                    )

                    binding.change.text = "+${String.format("%.2f", model.priceChange)}%"
                    binding.change.setTextColor(ContextCompat.getColor(context, R.color.green))
                } else {

                    binding.icGraph.setImageResource(
                        when(index % 2) {
                            0 -> R.drawable.ic_graph_negative_1
                            else -> R.drawable.ic_graph_negative_2
                        }
                    )

                    binding.change.text = "${String.format("%.2f", model.priceChange)}%"
                    binding.change.setTextColor(ContextCompat.getColor(context, R.color.gray))
                }


            }
        }

        override fun getDefaultLayout(): Int = R.layout.item_asset
    }
}
package com.investbuddy.features.investplan.ui

import android.view.View
import com.airbnb.epoxy.EpoxyAdapter
import com.airbnb.epoxy.EpoxyModel
import com.investbuddy.R
import com.investbuddy.common.room.invest.InvestDataDB
import com.investbuddy.databinding.ItemInvestListBinding
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.Locale

class InvestListAdapter(): EpoxyAdapter() {

    fun createList(items: List<InvestDataDB>, currency: String) {
        removeAllModels()

        items.map { addModel(InvestListModel(it, currency)) }
    }

    inner class InvestListModel(
        private val model: InvestDataDB,
        private val currency: String
    ): EpoxyModel<View>() {

        private var root: View? = null

        override fun bind(view: View) {
            super.bind(view)
            root = view

            with(view) {
                val binding = ItemInvestListBinding.bind(view)
                val formatOut = SimpleDateFormat("dd/MMM/yyyy", Locale.ENGLISH)
                val formatIn = SimpleDateFormat("MMMM, dd", Locale.ENGLISH)

                val date = formatOut.parse(model.date)
                binding.text.text = formatIn.format(date)
                binding.sum.text = "${model.amount.toString()}${currency}"
            }
        }

        override fun getDefaultLayout(): Int = R.layout.item_invest_list

    }
}
package com.investbuddy.features.investplan.ui

import android.view.View
import com.airbnb.epoxy.EpoxyAdapter
import com.airbnb.epoxy.EpoxyModel
import com.investbuddy.R
import com.investbuddy.databinding.ItemCalendarMonthBinding
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale

class InvestTypeAdapter(
    private val typeClickListener: (String) -> Unit
): EpoxyAdapter() {

    fun createList(items: List<String>) {
        removeAllModels()

        items.map { addModel(InvestTypeModel(it, typeClickListener)) }
    }

    inner class InvestTypeModel(
        private val model: String,
        private val clickListener: (String) -> Unit
    ): EpoxyModel<View>() {

        private var root: View? = null

        override fun bind(view: View) {
            super.bind(view)
            root = view

            with(view) {
                val binding = ItemCalendarMonthBinding.bind(this)


                binding.text.text = model

                binding.text.setOnClickListener {
                    clickListener.invoke(model)
                }
            }
        }

        override fun getDefaultLayout(): Int = R.layout.item_calendar_month

    }
}
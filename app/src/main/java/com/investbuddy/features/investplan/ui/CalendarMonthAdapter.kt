package com.investbuddy.features.investplan.ui

import android.view.View
import com.airbnb.epoxy.EpoxyAdapter
import com.airbnb.epoxy.EpoxyModel
import com.investbuddy.R
import com.investbuddy.databinding.ItemCalendarMonthBinding
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale

class CalendarMonthAdapter(
    private val monthClickListener: (YearMonth) -> Unit
) : EpoxyAdapter() {

    fun createList(items: List<YearMonth>) {
        removeAllModels()

        items.map { addModel(CalendarMonthModel(it, monthClickListener)) }
    }

    inner class CalendarMonthModel(
        private val yearMonth: YearMonth,
        private val monthClickListener: (YearMonth) -> Unit
    ) : EpoxyModel<View>() {

        private var root: View? = null

        override fun bind(view: View) {
            super.bind(view)
            root = view

            with(view) {
                val binding = ItemCalendarMonthBinding.bind(this)


                binding.text.text = YearMonth.of(yearMonth.year, yearMonth.monthValue)
                    .format(
                        DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH)
                    )

                binding.text.setOnClickListener {
                    monthClickListener.invoke(yearMonth)
                }
            }
        }

        override fun getDefaultLayout(): Int = R.layout.item_calendar_month
    }
}
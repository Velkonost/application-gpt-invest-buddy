package com.investbuddy.features.investplan.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.text.method.DigitsKeyListener
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.investbuddy.R
import com.investbuddy.common.di.IOnBackPressed
import com.investbuddy.common.event.ChangeNavViewVisibilityEvent
import com.investbuddy.core.base.ui.BaseFragment
import com.investbuddy.core.extension.observe
import com.investbuddy.databinding.FragmentInvestPlanBinding
import com.investbuddy.features.calendarview.CalendarDay
import com.investbuddy.features.calendarview.MaterialCalendarView.SHOW_NONE
import dagger.hilt.android.AndroidEntryPoint
import eightbitlab.com.blurview.RenderEffectBlur
import eightbitlab.com.blurview.RenderScriptBlur
import org.greenrobot.eventbus.EventBus
import org.threeten.bp.DayOfWeek
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale


@AndroidEntryPoint
class InvestPlanFragment : BaseFragment(R.layout.fragment_invest_plan), IOnBackPressed {

    private val binding by viewBinding(FragmentInvestPlanBinding::bind)

    private val viewModel: InvestPlanViewModel by viewModels()

    private lateinit var addInvestBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private var lastSelectedDate: CalendarDay? = null

    private val calendarMonthAdapter: CalendarMonthAdapter by lazy {
        CalendarMonthAdapter {
            binding.calendarView.setCurrentDate(it.atDay(1))
            viewModel.updateMonth(it.month.value, it.year)

            binding.blurView.isVisible = false
            binding.monthContainer.isVisible = false
            EventBus.getDefault().post(ChangeNavViewVisibilityEvent(isVisible = true))
        }
    }

    private val investTypeAdapter: InvestTypeAdapter by lazy {
        InvestTypeAdapter { result ->
            with(binding) {
                blurView.isVisible = false
                investTypeContainer.isVisible = false
                EventBus.getDefault().post(ChangeNavViewVisibilityEvent(isVisible = true))

                lastSelectedDate?.let { showAddInvestBottomSheet(it, result, binding.addInvest.amount.text.toString()) }
            }
        }
    }

    private val investListAdapter: InvestListAdapter by lazy { InvestListAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe(viewModel.viewState) { handleViewState(it) }
        initData()
        initViews()
    }

    private fun initViews() {
        initCalendar()
        setupBlurView()
        setupAddInvestBottomSheet()

        with(binding.monthRecycler) {
            adapter = calendarMonthAdapter
        }

        with(binding.investTypeRecycler) {
            adapter = investTypeAdapter
        }

        with(binding.listRecycler) {
            adapter = investListAdapter
        }

        with(binding) {
            sum.isSelected = true
//            sum.startScroll()

            icCloseTip.setOnClickListener {
                viewModel.hideTip()
            }
        }
    }

    private fun handleViewState(viewState: InvestPlanViewState) {
        with(binding) {

            warningBlock.isVisible = viewState.isTipEnabled

            sumCurrency.text = viewState.currencySymbol

            daysMonth.text = "at ${viewState.monthName}"
            sumMonth.text = "at ${viewState.monthName}"
            listTitle.text =
                "Investing in ${viewState.monthName.replaceFirstChar { it.uppercase() }}"

            days.text = viewState.days.toString()
            sum.text = String.format("%.2f", viewState.amount).replace(",", ".")

            addInvest.currency.setText(viewState.currency)

            investTypeAdapter.createList(viewState.types)

            tryEnableInvestBtn()

            investListAdapter.createList(viewState.listItems, viewState.currencySymbol)
        }

        with(binding.calendarView) {
            clearSelection()

            val today = CalendarDay.today()

            viewState.dates.forEach { date ->
                setDateSelected(date, true)
            }


            currentDate
        }
    }

    private fun initCalendar() {
        with(binding.calendarView) {
            showOtherDates = SHOW_NONE

            isDynamicHeightEnabled = true

            currentDate = CalendarDay.today()

            setWeekDayFormatter { dayOfWeek ->
                when (dayOfWeek) {
                    DayOfWeek.MONDAY -> "M"
                    DayOfWeek.TUESDAY -> "T"
                    DayOfWeek.WEDNESDAY -> "W"
                    DayOfWeek.THURSDAY -> "T"
                    DayOfWeek.FRIDAY -> "F"
                    DayOfWeek.SATURDAY -> "S"
                    else -> "S"
                }
            }

            setAllowClickDaysOutsideCurrentMonth(false)

            setOnTitleClickListener {
                binding.blurView.isVisible = true
                binding.monthContainer.isVisible = true
                EventBus.getDefault().post(ChangeNavViewVisibilityEvent(isVisible = false))
            }

            setOnDateClickListener { widget, date ->
                if (!selectedDates.contains(date)) {
                    showAddInvestBottomSheet(date)
                }
            }

            setOnMonthChangedListener { widget, date ->
                viewModel.updateMonth(date.month, date.year)
            }

            viewModel.updateMonth(currentDate.month, currentDate.year)

        }
    }

    private fun initData() {
        with(calendarMonthAdapter) {
            createList(getMonthsOfCurrentYear())
        }
    }

    private fun getMonthsOfCurrentYear(): List<YearMonth> {
        val currentMonth: YearMonth = YearMonth.now()
        val yearMonths: MutableList<YearMonth> = ArrayList<YearMonth>()
        for (month in 1..12) { //currentMonth.monthValue + 1
            yearMonths.add(YearMonth.of(currentMonth.year, month))
        }
        return yearMonths
    }

    private fun setupBlurView() {
        val radius = 1f

        val decorView: View = requireActivity().window.decorView
        val rootView: ViewGroup = decorView.findViewById(android.R.id.content)
        val windowBackground: Drawable = decorView.background

        binding.blurView.setupWith(
            rootView,
            RenderScriptBlur(requireContext()),
//            RenderEffectBlur()
        ) // or RenderEffectBlur
            .setFrameClearDrawable(windowBackground)
            .setBlurRadius(radius)
    }

    private fun changeBlurState(isVisible: Boolean) {
        binding.blurView.isVisible = isVisible
    }

    private fun showAddInvestBottomSheet(calendarDay: CalendarDay, selectedType: String = "", selectedAmount: String = "") {
        addInvestBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        lastSelectedDate = calendarDay

        with(binding.addInvest) {
            type.setText(selectedType)
            amount.setText(selectedAmount)

            val formatter: DateTimeFormatter =
                DateTimeFormatter.ofPattern("MMMM, dd", Locale.ENGLISH)
            date.text = calendarDay.date.format(formatter)

            icClose.setOnClickListener {
                addInvestBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }

            amount.keyListener = DigitsKeyListener.getInstance(true,true)

            type.setOnClickListener {
                addInvestBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

                Handler().postDelayed({
                    binding.blurView.isVisible = true
                    binding.investTypeContainer.isVisible = true
                    EventBus.getDefault().post(ChangeNavViewVisibilityEvent(isVisible = false))
                }, 500)

            }

            amount.doOnTextChanged { text, start, before, count ->
                tryEnableInvestBtn()
            }

            addBtn.setOnClickListener {
                if (tryEnableInvestBtn()) {
                    val formatterDB: DateTimeFormatter =
                        DateTimeFormatter.ofPattern("dd/MMM/yyyy", Locale.ENGLISH)
                    val investDate = calendarDay.date.format(formatterDB)

                    viewModel.addDate(
                        date = investDate,
                        amount = amount.text.toString().replace(",", ".").toFloat(),
                        type = type.text.toString(),
                        monthNumber = calendarDay.month,
                        yearNumber = calendarDay.year
                    )

                    addInvestBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }

            tryEnableInvestBtn()
        }
    }

    private fun tryEnableInvestBtn() =
        with(binding.addInvest) {
            val isEnable =
                amount.text.isNotEmpty() && currency.text.isNotEmpty() && type.text.isNotEmpty()
                        && amount.text.toString().toFloat() != 0f

            addBtn.background = ContextCompat.getDrawable(
                requireContext(),
                if (isEnable) R.drawable.bg_add_invest_btn
                else R.drawable.bg_add_invest_btn_disabled
            )

            isEnable
        }

    private fun setupAddInvestBottomSheet() {
        addInvestBottomSheetBehavior =
            BottomSheetBehavior.from(binding.addInvest.bottomSheetContainer).apply {
                addBottomSheetCallback(object :
                    BottomSheetBehavior.BottomSheetCallback() {
                    override fun onSlide(bottomSheet: View, slideOffset: Float) {}

                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                            EventBus.getDefault().post(ChangeNavViewVisibilityEvent(true))
                            changeBlurState(isVisible = false)
//                            binding.sum.resumeScroll()
                            binding.sum.ellipsize = TextUtils.TruncateAt.MARQUEE

                        } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                            EventBus.getDefault().post(ChangeNavViewVisibilityEvent(false))
                            changeBlurState(isVisible = true)
                            binding.sum.ellipsize = null
//                            binding.sum.pauseScroll()
                        }
                    }
                })
            }
    }

    override fun onBackPressed(): Boolean =
        if (addInvestBottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            addInvestBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            true
        } else if (binding.monthContainer.isVisible) {
            binding.blurView.isVisible = false
            binding.monthContainer.isVisible = false
            EventBus.getDefault().post(ChangeNavViewVisibilityEvent(isVisible = true))
            true
        } else if (binding.investTypeContainer.isVisible) {
            binding.blurView.isVisible = false
            EventBus.getDefault().post(ChangeNavViewVisibilityEvent(isVisible = true))
            binding.investTypeContainer.isVisible = false

            lastSelectedDate?.let { showAddInvestBottomSheet(it, selectedAmount = binding.addInvest.amount.text.toString()) }
            true
        } else {
            false
        }


}
package com.project.rezsim.ui.screen.overview

import android.app.DatePickerDialog
import android.graphics.PorterDuff
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.postDelayed
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.rezsim.R
import com.project.rezsim.base.RezsimFragment
import com.project.rezsim.device.*
import com.project.rezsim.server.dto.calculation.Calculation
import com.project.rezsim.server.dto.measurement.Measurement
import com.project.rezsim.server.dto.measurement.Utility
import com.project.rezsim.tool.DateHelper
import com.project.rezsim.ui.screen.activity.MainActivityViewModel
import com.project.rezsim.ui.screen.header.HeaderViewModel
import com.project.rezsim.ui.screen.main.MainFragment
import com.project.rezsim.ui.screen.overview.list.MeasurementAdapter
import com.project.rezsim.ui.screen.overview.list.MeasurementMarginDecoration
import com.project.rezsim.ui.view.message.MessageSeverity
import com.project.rezsim.ui.view.message.MessageType
import org.koin.android.ext.android.inject
import java.util.*

class OverviewFragment : RezsimFragment() {

    override val contentId: Int = R.layout.overview_fragment

    private val viewModel: OverviewViewModel by inject()
    private val activityViewModel: MainActivityViewModel by inject()
    private val headerViewModel: HeaderViewModel by inject()
    private val drawableRepository: DrawableRepository by inject()
    private val stringRepository: StringRepository by inject()
    private val colorRepository: ColorRepository by inject()

    private lateinit var layoutDateSelector: ConstraintLayout
    private lateinit var yearButton: AppCompatButton
    private val monthSelectorButtons = mutableListOf<AppCompatButton>()
    private lateinit var measurementAdapter: MeasurementAdapter
    private lateinit var paymentCard: CardView
    private lateinit var paymentUnderText: AppCompatTextView
    private lateinit var comsumptionUnderText: AppCompatTextView
    private lateinit var paymentAboveText: AppCompatTextView
    private lateinit var comsumptionAboveText: AppCompatTextView
    private lateinit var paymentFullText: AppCompatTextView
    private lateinit var comsumptionFullText: AppCompatTextView

    private val itemDecoration = MeasurementMarginDecoration()

    private val selectDateListener = object : DatePickerDialog.OnDateSetListener {
        override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        }
    }

    override fun setupViews() {
        super.setupViews()
        headerViewModel.setTitle(viewModel.title())
        view?.let {
            layoutDateSelector = it.findViewById(R.id.clDateSelector)
            yearButton = it.findViewById<AppCompatButton>(R.id.btYear).apply {
                isSelected = true
//                setOnClickListener { selectDate() }
            }
            paymentCard = it.findViewById(R.id.cvPayment)
            paymentUnderText = it.findViewById(R.id.tvPaymentValueUnderLimit)
            paymentAboveText = it.findViewById(R.id.tvPaymentValueAboveLimit)
            comsumptionUnderText = it.findViewById(R.id.tvPaymentConsunptionUnderLimit)
            comsumptionAboveText = it.findViewById(R.id.tvPaymentConsunptionAboveLimit)
            paymentFullText = it.findViewById(R.id.tvPaymentValue)
            comsumptionFullText = it.findViewById(R.id.tvPaymentConsunption)

            viewModel.init()
            it.findViewById<RecyclerView>(R.id.rvMeters).apply {
                layoutManager = LinearLayoutManager(requireContext())
                measurementAdapter = MeasurementAdapter()
                adapter = measurementAdapter
                removeItemDecoration(itemDecoration)
                addItemDecoration(itemDecoration)
            }
            paymentCard.backgroundTintList = colorRepository.stateList(viewModel.paymentBackground())
        }
        refreshMonthSelector()
        monthSelectorButtons.last().performClick()
    }

    override fun subscribeObservers() {
        super.subscribeObservers()
        viewModel.monthSelectorVisibilityLiveData.observe (this) { setMonthSelectorVisibility(it) }
        viewModel.paymentVisibilityLiveData.observe(this) { setPaymentVisibility(it) }
        headerViewModel.backLiveData.observeForever { goBack(it) }
        headerViewModel.buttonPressedLiveData.observe(this) { viewModel.headerButtonPressed(it) }
        viewModel.meterItemsLiveData.observe(this) { setMeterItems(it) }
        activityViewModel.fabPressedLiveData.observe(this) {
            if (it) {
                activityViewModel.clearFabLiveData()
                viewModel.readMeter()
            }
        }
        viewModel.reinitLiveData.observe(this) { setupViews() }
        viewModel.calculationLiveData.observe(this) { showCalculation(it) }
        viewModel.refreshMonthSelectorLiveData.observe(this) { refreshMonthSelector(it) }
    }

    private fun setMonthSelectorVisibility(visible: Boolean) {
        layoutDateSelector.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun setPaymentVisibility(visible: Boolean) {
        (if (visible) View.VISIBLE else View.GONE).let {
            paymentCard.visibility = it
            paymentUnderText.visibility = it
            paymentAboveText.visibility = it
        }

    }

    private fun refreshMonthSelector(selected: Month? = null) {
        view?.let {
            val layout: LinearLayout = it.findViewById(R.id.llMonths)
            monthSelectorButtons.clear()
            layout.removeAllViews()
            viewModel.months().forEachIndexed { index, month ->
                val button = createMonthButton(index, month)
                monthSelectorButtons.add(button)
                layout.addView(button)
            }
            it.findViewById<HorizontalScrollView>(R.id.swMonths).let {
                it.postDelayed({
                    it.fullScroll(View.FOCUS_RIGHT)
                }, 100)
            }
            selected?.let {
                val button = monthSelectorButtons[viewModel.months().indexOf(it)]
                button.performClick()
            }
        }
    }

    private fun createMonthButton(id: Int, month: Month) = AppCompatButton(requireContext()).apply {
        setId(id)
        layoutParams =  ViewGroup.MarginLayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT).apply {
            setMargins(4.dp, 0, 4.dp, 0)
        }
        background = drawableRepository.getById(R.drawable.statelist_button_background)
        text = stringRepository.getByName("overview_month_${month.month}")
        setTextColor(colorRepository.color(R.color.button_text_color_dark))
        textAlignment = View.TEXT_ALIGNMENT_CENTER
        setPadding(8.dp, 0, 8.dp, 0)
        backgroundTintList = colorRepository.stateList(R.color.material_grey_5)
        backgroundTintMode = PorterDuff.Mode.ADD
        isAllCaps = false
        setOnClickListener { v ->
            monthSelectorButtons.forEach {
                it.isSelected = it.id == v.id
            }
            yearButton.text = viewModel.months()[v.id].year.toString()
            viewModel.selectMonth(v.id)
        }
    }

    private fun selectDate() {
        val calendar = DateHelper.now()
        DatePickerDialog(
            requireContext(),
            selectDateListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.apply {
                viewModel.minDate()?.let {
                    minDate = it
                }
            }
        }.show()
    }

    private fun goBack(value: Boolean) {
        if (value && activityViewModel.currentFragmentTag() == TAG) {
            headerViewModel.clearBackLiveData()
            activityViewModel.switchToFragment(MainFragment.TAG)
        }
    }

    private fun setMeterItems(measurements: List<Measurement>) {
        measurementAdapter.setItems(measurements)
    }

    private fun showCalculation(calculation: Calculation?) {
        if (calculation != null) {
            paymentUnderText.text = stringRepository.getById(R.string.payment_value, calculation.discountPrice.divThousands)
            paymentAboveText.text = stringRepository.getById(R.string.payment_value, calculation.fullPrice.divThousands)
            paymentFullText.text = stringRepository.getById(R.string.payment_value, (calculation.discountPrice + calculation.fullPrice).divThousands)
            val comsumptionResId = if (viewModel.utility == Utility.GAS) R.string.gas_value else R.string.electricity_value
            comsumptionUnderText.text = stringRepository.getById(comsumptionResId, calculation.underConsumption)
            comsumptionAboveText.text = stringRepository.getById(comsumptionResId, calculation.overConsumption)
            comsumptionFullText.text = stringRepository.getById(comsumptionResId, calculation.underConsumption + calculation.overConsumption)
        } else {
            activityViewModel.showMessage(null, stringRepository.getById(R.string.overview_calculation_error), MessageType.SNACKBAR_CLOSEABLE_AND_MANUALCLOSE, MessageSeverity.ERROR)
        }
    }

    companion object {
        const val TAG = "OverviewFragment"
        fun newinstance() = OverviewFragment()
    }
}
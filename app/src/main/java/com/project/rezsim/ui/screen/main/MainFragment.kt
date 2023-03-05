package com.project.rezsim.ui.screen.main

import android.graphics.PorterDuff
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import androidx.appcompat.widget.*
import androidx.cardview.widget.CardView
import com.project.rezsim.R
import com.project.rezsim.base.RezsimFragment
import com.project.rezsim.device.*
import com.project.rezsim.server.UserModel
import com.project.rezsim.server.dto.measurement.Level
import com.project.rezsim.server.dto.measurement.Measurement
import com.project.rezsim.server.dto.measurement.Utility
import com.project.rezsim.ui.screen.activity.MainActivityViewModel
import com.project.rezsim.ui.screen.header.HeaderViewModel
import com.project.rezsim.ui.view.spinner.TextSpinnerAdapter
import com.project.rezsim.ui.view.spinner.TextSpinnerOnItemSelectedListener
import org.koin.android.ext.android.inject

class MainFragment : RezsimFragment() {

    override val contentId = R.layout.main_fragment

    private val viewModel: MainViewModel by inject()
    private val activityViewModel: MainActivityViewModel by inject()
    private val userModel: UserModel by inject()
    private val screenRepository: ScreenRepository by inject()
    private val stringRepository: StringRepository by inject()
    private val drawableRepository: DrawableRepository by inject()
    private val colorRepository: ColorRepository by inject()
    private val headerViewModel: HeaderViewModel by inject()

    private var spinnerHouseholds: AppCompatSpinner? = null

    private val householdsButtons = mutableListOf<AppCompatButton>()
    private var householdsButtonClickListener = object : View.OnClickListener {
        override fun onClick(button: View) {
            onClickHouseholdsButton(button as AppCompatButton)
        }

    }

    override fun setupViews() {
        super.setupViews()
        headerViewModel.setTitle(R.string.main_header_title)
        view?.let {
            it.findViewById<AppCompatButton>(R.id.btAddHousehold).setOnClickListener { viewModel.addHouseholdLiveData.value = true }
            it.findViewById<AppCompatButton>(R.id.btReadElectricity).setOnClickListener { viewModel.readMeter(Utility.ELECTRICITY_A) }
            it.findViewById<AppCompatButton>(R.id.btReadGas).setOnClickListener { viewModel.readMeter(Utility.GAS) }
            it.findViewById<CardView>(R.id.cwElectricity).setOnClickListener { activityViewModel.goOverviewScreen(viewModel.currentHousehold(), Utility.ELECTRICITY_A) }
            it.findViewById<CardView>(R.id.cwGas).setOnClickListener { activityViewModel.goOverviewScreen(viewModel.currentHousehold(), Utility.GAS) }
        }

        if (userModel.hasHousehold()) {
            refreshHouseholds()
        }
    }

    override fun subscribeObservers() {
        super.subscribeObservers()
        viewModel.electricityMeasurementLiveData.observe(this) { fillElectricityMeasurement(it) }
        viewModel.gasMeasurementLiveData.observe(this) { fillGasMeasurement(it) }
        viewModel.refreshHouseholdsLiveData.observe(this) { refreshHouseholds() }
    }

    private fun refreshHouseholds() {
        view?.let {
            if (screenRepository.isTablet()) {
                val layout: LinearLayout = it.findViewById(R.id.llHouseholds)
                layout.removeAllViews()
                householdsButtons.clear()
                viewModel.householdItems().forEachIndexed { index, s ->
                    if (index > 0) {
                        val button = createHouseholdButton(index, s)
                        householdsButtons.add(button)
                        layout.addView(button)
                    }
                }
                val scroll = it.findViewById<HorizontalScrollView>(R.id.swHouseholds)
                val button = householdsButtons[viewModel.currentHousehold()]
                scroll.post { scroll.scrollTo(button.left, 0) }
                button.performClick()
            } else {
                spinnerHouseholds = it.findViewById<AppCompatSpinner?>(R.id.spHousehold).apply {
                    adapter = TextSpinnerAdapter(requireContext(), viewModel.householdItems())
                    onItemSelectedListener = TextSpinnerOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            viewModel.householdSelected(position)
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {}
                    })
                    setSelection(viewModel.currentHousehold() + 1)
                }
            }
        }
    }

    private fun fillElectricityMeasurement(data: Pair<Boolean, Measurement?>) {
        view?.let {
            val visible = data.first
            val measurement = data.second
            it.findViewById<CardView>(R.id.cwElectricity).visibility = if (visible) View.VISIBLE else View.GONE
            it.findViewById<AppCompatTextView>(R.id.tvTitleLastMeterElectricity).visibility = if (measurement != null) View.VISIBLE else View.INVISIBLE
            it.findViewById<AppCompatTextView>(R.id.tvTitleMonthlyConsumptionElectricity).visibility = if (measurement != null) View.VISIBLE else View.INVISIBLE
            it.findViewById<AppCompatTextView>(R.id.tvElectricityEmpty).visibility = if (measurement == null) View.VISIBLE else View.INVISIBLE
            if (measurement != null) {
                val color = if (measurement.level == Level.UNDERLIMIT.value) R.color.normal_comsumption_color else R.color.overhead_comsumption_color
                it.findViewById<AppCompatTextView>(R.id.tvValueLastMeterElectricity).apply {
                    text = stringRepository.getById(R.string.electricity_value, measurement.position)
                    visibility = View.VISIBLE
                }
                it.findViewById<AppCompatTextView>(R.id.tvDateLastMeterElectricity).apply {
                    text = measurement.date.substring(0, 10)
                    visibility = View.VISIBLE
                }
                it.findViewById<AppCompatTextView>(R.id.tvValueMonthlyConsumptionElectricity).apply {
                    text = stringRepository.getById(R.string.electricity_value, measurement.consumption)
                    visibility = View.VISIBLE
                    setTextColor(colorRepository.color(color))
                }
                it.findViewById<AppCompatImageView>(R.id.ivIconElectricity).imageTintList = colorRepository.stateList(color)
            } else {
                it.findViewById<AppCompatTextView>(R.id.tvValueLastMeterElectricity).visibility = View.INVISIBLE
                it.findViewById<AppCompatTextView>(R.id.tvDateLastMeterElectricity).visibility = View.INVISIBLE
                it.findViewById<AppCompatTextView>(R.id.tvValueMonthlyConsumptionElectricity).visibility = View.INVISIBLE
                it.findViewById<AppCompatImageView>(R.id.ivIconElectricity).imageTintList = colorRepository.stateList(R.color.text_color)
            }
        }
    }

    private fun fillGasMeasurement(data: Pair<Boolean, Measurement?>) {
        view?.let {
            val visible = data.first
            val measurement = data.second
            it.findViewById<CardView>(R.id.cwGas).visibility = if (visible) View.VISIBLE else View.GONE
            it.findViewById<AppCompatTextView>(R.id.tvTitleLastMeterGas).visibility = if (measurement != null) View.VISIBLE else View.INVISIBLE
            it.findViewById<AppCompatTextView>(R.id.tvTitleMonthlyConsumptionGas).visibility = if (measurement != null) View.VISIBLE else View.INVISIBLE
            it.findViewById<AppCompatTextView>(R.id.tvGasEmpty).visibility = if (measurement == null) View.VISIBLE else View.INVISIBLE

            if (measurement != null) {
                val color = if (measurement.level == Level.UNDERLIMIT.value) R.color.normal_comsumption_color else R.color.overhead_comsumption_color
                it.findViewById<AppCompatTextView>(R.id.tvValueLastMeterGas).apply {
                    text = stringRepository.getById(R.string.gas_value, measurement.position)
                    visibility = View.VISIBLE
                }
                it.findViewById<AppCompatTextView>(R.id.tvDateLastMeterGas).apply {
                    text = measurement.date.substring(0, 10)
                    visibility = View.VISIBLE
                }
                it.findViewById<AppCompatTextView>(R.id.tvValueMonthlyConsumptionGas).apply {
                    text = stringRepository.getById(R.string.gas_value, measurement.consumption)
                    visibility = View.VISIBLE
                    setTextColor(colorRepository.color(color))
                }
                it.findViewById<AppCompatImageView>(R.id.ivIconGas).imageTintList = colorRepository.stateList(color)
            } else {
                it.findViewById<AppCompatTextView>(R.id.tvValueLastMeterGas).visibility = View.INVISIBLE
                it.findViewById<AppCompatTextView>(R.id.tvDateLastMeterGas).visibility = View.INVISIBLE
                it.findViewById<AppCompatTextView>(R.id.tvValueMonthlyConsumptionGas).visibility = View.INVISIBLE
                it.findViewById<AppCompatImageView>(R.id.ivIconGas).imageTintList = colorRepository.stateList(R.color.text_color)
            }
        }
    }

    private fun createHouseholdButton(id: Int, text: String) = AppCompatButton(requireContext()).apply {
        setId(id)
        layoutParams =  ViewGroup.MarginLayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT).apply {
            setMargins(0, 0, 8.dp, 0)
        }
        background = drawableRepository.getById(R.drawable.statelist_button_background)
        setText(text)
        setTextColor(colorRepository.color(R.color.button_text_color_dark))
        textAlignment = View.TEXT_ALIGNMENT_CENTER
        setPadding(8.dp, 0, 8.dp, 0)
        backgroundTintList = colorRepository.stateList(R.color.material_grey_5)
        backgroundTintMode = PorterDuff.Mode.ADD
        isAllCaps = false
        setOnClickListener(householdsButtonClickListener)
    }

    private fun onClickHouseholdsButton(button: AppCompatButton) {
        householdsButtons.forEach {
            it.isSelected = it.id == button.id
        }
        viewModel.householdSelected(householdsButtons.indexOf(button))
    }

    companion object {
        const val TAG = "MainFragment"
        fun newInstance() = MainFragment()
    }

}
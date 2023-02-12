package com.project.rezsim.ui.screen.main

import android.content.DialogInterface
import android.graphics.PorterDuff
import android.text.Layout
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.appcompat.widget.*
import androidx.core.content.res.ResourcesCompat
import com.project.rezsim.R
import com.project.rezsim.base.RezsimFragment
import com.project.rezsim.device.ScreenRepository
import com.project.rezsim.device.StringRepository
import com.project.rezsim.device.dp
import com.project.rezsim.server.dto.Household
import com.project.rezsim.server.dto.Measurement
import com.project.rezsim.ui.view.spinner.TextSpinnerAdapter
import com.project.rezsim.ui.view.spinner.TextSpinnerOnItemSelectedListener
import org.koin.android.ext.android.inject

class MainFragment : RezsimFragment() {

    override val contentId = R.layout.main_fragment

    private val viewModel: MainViewModel by inject()
    private val screenRepository: ScreenRepository by inject()
    private val stringRepository: StringRepository by inject()

    private var spinnerHouseholds: AppCompatSpinner? = null

    private val householdsButtons = mutableListOf<AppCompatButton>()
    private var householdsButtonClickListener = object : View.OnClickListener {
        override fun onClick(button: View) {
            onClickHouseholdsButton(button as AppCompatButton)
        }

    }

    override fun setupViews() {
        super.setupViews()
        view?.let {
            it.findViewById<AppCompatButton>(R.id.btAddHousehold).setOnClickListener { viewModel.addHouseholdLiveData.value = true }
            if (screenRepository.isTablet()) {
                val layout: LinearLayout = it.findViewById(R.id.llHouseholds)
                viewModel.householdItems().forEachIndexed { index, s ->
                    if (index > 0) {
                        val button = createHouseholdButton(index, s)
                        householdsButtons.add(button)
                        layout.addView(button)
                    }
                }
                householdsButtons[0].performClick()
            } else {
                spinnerHouseholds = it.findViewById<AppCompatSpinner?>(R.id.spHousehold).apply {
                    adapter = TextSpinnerAdapter(requireContext(), viewModel.householdItems())
                    onItemSelectedListener = TextSpinnerOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            viewModel.householdSelected(position)
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {}
                    })
                    setSelection(1)
                }
            }
        }
    }

    override fun subscribeObservers() {
        super.subscribeObservers()
        viewModel.electricityMeasurementLiveData.observe(this) { fillElectricityMeasurement(it) }
        viewModel.gasMeasurementLiveData.observe(this) { fillGasMeasurement(it) }
    }

    private fun fillElectricityMeasurement(measurement: Measurement) {
        view?.let {
            it.findViewById<AppCompatTextView>(R.id.tvValueLastMeterElectricity).text = stringRepository.getById(R.string.electricity_value, measurement.position)
            it.findViewById<AppCompatTextView>(R.id.tvDateLastMeterElectricity).text = measurement.date.substring(0, 10)
            it.findViewById<AppCompatTextView>(R.id.tvValueMonthlyConsumptionElectricity).text = stringRepository.getById(R.string.electricity_value, measurement.consumption)
        }
    }

    private fun fillGasMeasurement(measurement: Measurement) {
        view?.let {
            it.findViewById<AppCompatTextView>(R.id.tvValueLastMeterGas).text = stringRepository.getById(R.string.gas_value, measurement.position)
            it.findViewById<AppCompatTextView>(R.id.tvDateLastMeterGas).text = measurement.date.substring(0, 10)
            it.findViewById<AppCompatTextView>(R.id.tvValueMonthlyConsumptionGas).text = stringRepository.getById(R.string.gas_value, measurement.consumption)
        }
    }

    private fun createHouseholdButton(id: Int, text: String) = AppCompatButton(requireContext()).apply {
        setId(id)
        layoutParams =  ViewGroup.MarginLayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT).apply {
            setMargins(0, 0, 8.dp, 0)
        }
        background = ResourcesCompat.getDrawable(resources, R.drawable.statelist_button_background, null)
        setText(text)
        setTextColor(resources.getColor(R.color.button_text_color_dark))
        textAlignment = View.TEXT_ALIGNMENT_CENTER
        setPadding(0, 0, 0, 0)
        backgroundTintList = resources.getColorStateList(R.color.material_grey_5, null)
        backgroundTintMode = PorterDuff.Mode.ADD
        isAllCaps = false
        setOnClickListener(householdsButtonClickListener)
    }

    private fun onClickHouseholdsButton(button: AppCompatButton) {
        householdsButtons.forEach {
            it.isEnabled = it.id != button.id
        }
        viewModel.householdSelected(householdsButtons.indexOf(button))
    }

    companion object {
        const val TAG = "MainFragment"
        fun newInstance() = MainFragment()
    }

}
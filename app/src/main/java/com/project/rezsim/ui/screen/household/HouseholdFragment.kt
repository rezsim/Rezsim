package com.project.rezsim.ui.screen.household

import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.project.rezsim.R
import com.project.rezsim.base.RezsimFragment
import com.project.rezsim.tool.numericValue
import com.project.rezsim.ui.screen.activity.MainActivityViewModel
import com.project.rezsim.ui.view.spinner.TextSpinnerAdapter
import com.project.rezsim.ui.view.spinner.TextSpinnerOnItemSelectedListener
import com.project.server.dto.Household
import org.koin.android.ext.android.inject

class HouseholdFragment : RezsimFragment() {

    override val contentId = R.layout.household_fragment

    private val viewModel: HouseholdViewModel by inject()
    private val activityViewModel: MainActivityViewModel by inject()

    private lateinit var textViewTitle: AppCompatTextView
    private lateinit var spinnerUsage: AppCompatSpinner
    private lateinit var spinnerPricingTypeA: AppCompatSpinner
    private lateinit var spinnerPricingTypeB: AppCompatSpinner
    private lateinit var buttonChildrenMinus: AppCompatImageButton
    private lateinit var buttonChildrenPlus: AppCompatImageButton
    private lateinit var editChildren: AppCompatEditText


    private val spinnerItemSelectedListener = TextSpinnerOnItemSelectedListener()

    override fun setupViews() {
        super.setupViews()
        view?.let {
            textViewTitle = it.findViewById<AppCompatTextView>(R.id.tvTitle).apply {
                setText(if (viewModel.isCreatingNew()) R.string.household_title_create else R.string.household_title_change)
            }
            spinnerUsage = it.findViewById<AppCompatSpinner?>(R.id.spUsage).apply {
                adapter = TextSpinnerAdapter(requireContext(), viewModel.usageItems())
                onItemSelectedListener = spinnerItemSelectedListener
            }
            spinnerPricingTypeA = it.findViewById<AppCompatSpinner?>(R.id.spPricingTypeA).apply {
                adapter = TextSpinnerAdapter(requireContext(), viewModel.pricingTypeAItems())
                onItemSelectedListener = spinnerItemSelectedListener
            }
            spinnerPricingTypeB = it.findViewById<AppCompatSpinner?>(R.id.spPricingTypeB).apply {
                adapter = TextSpinnerAdapter(requireContext(), viewModel.pricingTypeBItems())
                onItemSelectedListener = spinnerItemSelectedListener
            }
            buttonChildrenMinus = it.findViewById<AppCompatImageButton>(R.id.btChildrenMinus).apply {
                setOnClickListener { viewModel.childrenButtonClicked(editChildren.text.toString(), -1) }
                isEnabled = false
            }
            buttonChildrenPlus = it.findViewById<AppCompatImageButton>(R.id.btChildrenPlus).apply {
                setOnClickListener { viewModel.childrenButtonClicked(editChildren.text.toString(), 1) }
            }
            editChildren = it.findViewById<AppCompatEditText>(R.id.etChildrenCount).apply {
                addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun afterTextChanged(p0: Editable?) {
                        val value = p0?.toString()?.toInt()
                        buttonChildrenMinus.isEnabled = value != null && value > 0
                    }
                })
            }
        }
    }

    override fun subscribeObservers() {
        super.subscribeObservers()
        activityViewModel.fabPressedLiveData.observe(this) { save() }
        viewModel.childrenValueLiveData.observe(this) {editChildren.setText(it.toString())}
    }

    private fun setContent() {

    }

    private fun save() {
        val content = collectValues()
        viewModel.save(content)
    }

    private fun collectValues(): Household =
        view?.let {
            Household(
                id = -1,
                userId = -1,
                name = it.findViewById<AppCompatEditText>(R.id.etName).text.toString(),
                electricityUsage = spinnerUsage.selectedItemPosition,
                electricityService = -1,
                electricityPricingTypeA = spinnerPricingTypeA.selectedItemPosition,
                electricityPricingTypeB = spinnerPricingTypeB.selectedItemPosition,
                electricityPricingTypeH = -1,
                electricityConsumptionUnit = -1,
                gasService = -1,
                gasChildren = editChildren.numericValue() ?: -1,
                gasConsumptionUnit = -1,
                gasHeatingValue = it.findViewById<AppCompatEditText>(R.id.etHeatingValue).numericValue() ?: -1,
                measurements = listOf()
            )
        } ?: error("View is null when collect values")



    companion object {
        const val TAG = "HouseholdFragment"
        fun newInstance() = HouseholdFragment()
    }

}
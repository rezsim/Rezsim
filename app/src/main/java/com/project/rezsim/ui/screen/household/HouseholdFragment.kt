package com.project.rezsim.ui.screen.household

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.project.rezsim.R
import com.project.rezsim.base.RezsimFragment
import com.project.rezsim.tool.numericValue
import com.project.rezsim.ui.screen.activity.MainActivityViewModel
import com.project.rezsim.ui.view.spinner.TextSpinnerAdapter
import com.project.rezsim.ui.view.spinner.TextSpinnerOnItemSelectedListener
import org.koin.android.ext.android.inject

class HouseholdFragment : RezsimFragment() {

    override val contentId = R.layout.household_fragment

    private val viewModel: HouseholdViewModel by inject()
    private val activityViewModel: MainActivityViewModel by inject()

    private lateinit var textViewTitle: AppCompatTextView
    private lateinit var editName: AppCompatEditText
    private lateinit var layoutMeters: ConstraintLayout
    private lateinit var spinnerUsage: AppCompatSpinner
    private lateinit var spinnerPricingTypeA: AppCompatSpinner
    private lateinit var spinnerPricingTypeB: AppCompatSpinner
    private lateinit var buttonChildrenMinus: AppCompatImageButton
    private lateinit var buttonChildrenPlus: AppCompatImageButton
    private lateinit var editChildren: AppCompatEditText
    private lateinit var editGasHeating: AppCompatEditText


    private val spinnerItemSelectedListener = TextSpinnerOnItemSelectedListener()

    override fun setupViews() {
        super.setupViews()
        view?.let {
            textViewTitle = it.findViewById(R.id.tvTitle)
            editName = it.findViewById(R.id.etName)
            layoutMeters = it.findViewById(R.id.clMeters)
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
            editGasHeating = it.findViewById(R.id.etHeatingValue)
        }
    }

    override fun subscribeObservers() {
        super.subscribeObservers()
        Log.d("DEBINFO-R", "activityViewModel.fabPressedLiveData start observing")
        activityViewModel.fabPressedLiveData.observe(this) {
            Log.d("DEBINFO-R", "activityViewModel.fabPressedLiveData triggered: $it")
            save()
        }
        viewModel.childrenValueLiveData.observe(this) { editChildren.setText(it.toString()) }
        viewModel.contentLiveData.observe(this) { setContent(it) }
    }

    private fun setContent(content: Content?) {
        textViewTitle.setText(if (content == null) R.string.household_title_create else R.string.household_title_change)
        layoutMeters.visibility = if (content == null) View.VISIBLE else View.GONE
        content?.let {
            editName.setText(it.name)
            spinnerUsage.setSelection(it.electricityUseMode + 1)
            spinnerPricingTypeA.setSelection(it.electricityPricingA + 1)
            spinnerPricingTypeB.setSelection(it.electricityPricingB + 1)
            editGasHeating.setText(it.gasHeating.toString())
            editChildren.setText(it.gasChildren.toString())
        }
    }

    private fun save() {
        val content = collectValues()
        viewModel.save(content)
    }

    private fun collectValues(): Content =
        view?.let {
            Content(
                name = it.findViewById<AppCompatEditText>(R.id.etName).text.toString(),
                electricityMeter = it.findViewById<AppCompatEditText>(R.id.etElectricityMeter).numericValue() ?: -1,
                gasMeter = it.findViewById<AppCompatEditText>(R.id.etGasMeter).numericValue() ?: -1,
                electricityUseMode = spinnerUsage.selectedItemPosition - 1,
                electricityPricingA = spinnerPricingTypeA.selectedItemPosition - 1,
                electricityPricingB = spinnerPricingTypeB.selectedItemPosition - 1,
                gasChildren = editChildren.numericValue() ?: -1,
                gasHeating = it.findViewById<AppCompatEditText>(R.id.etHeatingValue).numericValue() ?: -1,
            )
        } ?: error("View is null when collect values")

    companion object {
        const val TAG = "HouseholdFragment"
        fun newInstance() = HouseholdFragment()
    }

}
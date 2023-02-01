package com.project.rezsim.ui.household

import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatSpinner
import com.project.rezsim.R
import com.project.rezsim.base.RezsimFragment
import com.project.rezsim.view.spinner.TextSpinnerAdapter
import com.project.rezsim.view.spinner.TextSpinnerOnItemSelectedListener
import org.koin.android.ext.android.inject

class HouseholdFragment : RezsimFragment() {

    override val contentId = R.layout.household_fragment

    private val viewModel: HouseholdViewModel by inject()

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
        viewModel.childrenValueLiveData.observe(this) {editChildren.setText(it.toString())}
    }


    companion object {
        const val TAG = "HouseholdFragment"
        fun newInstance() = HouseholdFragment()
    }

}
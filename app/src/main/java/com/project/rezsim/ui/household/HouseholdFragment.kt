package com.project.rezsim.ui.household

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


        }


    }



    companion object {
        const val TAG = "HouseholdFragment"
        fun newInstance() = HouseholdFragment()
    }

}
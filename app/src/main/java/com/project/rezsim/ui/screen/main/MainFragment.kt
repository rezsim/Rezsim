package com.project.rezsim.ui.screen.main

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatSpinner
import com.project.rezsim.R
import com.project.rezsim.base.RezsimFragment
import com.project.rezsim.device.ScreenRepository
import com.project.rezsim.server.dto.Household
import com.project.rezsim.ui.view.spinner.TextSpinnerAdapter
import com.project.rezsim.ui.view.spinner.TextSpinnerOnItemSelectedListener
import org.koin.android.ext.android.inject

class MainFragment : RezsimFragment() {

    override val contentId = R.layout.main_fragment

    private val viewModel: MainViewModel by inject()
    private val screenRepository: ScreenRepository by inject()

    private var spinnerHouseholds: AppCompatSpinner? = null

    override fun setupViews() {
        super.setupViews()
        view?.let {
            if (screenRepository.isTablet()) {
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

    companion object {
        const val TAG = "MainFragment"
        fun newInstance() = MainFragment()
    }

}
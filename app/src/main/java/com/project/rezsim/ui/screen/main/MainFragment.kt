package com.project.rezsim.ui.screen.main

import android.graphics.PorterDuff
import android.text.Layout
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.res.ResourcesCompat
import com.project.rezsim.R
import com.project.rezsim.base.RezsimFragment
import com.project.rezsim.device.ScreenRepository
import com.project.rezsim.device.dp
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
                val layout: LinearLayoutCompat = it.findViewById(R.id.llHouseholds)
                viewModel.householdItems().forEachIndexed { index, s ->
                    if (index > 0) {
                        val lp = ViewGroup.MarginLayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT).apply {
                            setMargins(0, 0, 100, 0)
                        }
                        layout.addView(createHouseholdButton(index, s), lp)
                    }
                }
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

    private fun createHouseholdButton(id: Int, text: String) = AppCompatButton(requireContext()).apply {
        setId(id)
        layoutParams =  ViewGroup.MarginLayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT).apply {
            setMargins(0, 0, 100, 0)
        }
        background = ResourcesCompat.getDrawable(resources, R.drawable.statelist_button_background, null)
        setText(text)
        setTextColor(resources.getColor(R.color.button_text_color))
        textAlignment = View.TEXT_ALIGNMENT_CENTER
        setPadding(0, 0, 0, 0)
        backgroundTintList = resources.getColorStateList(R.color.material_grey_8, null)
        backgroundTintMode = PorterDuff.Mode.ADD
    }

    companion object {
        const val TAG = "MainFragment"
        fun newInstance() = MainFragment()
    }

}
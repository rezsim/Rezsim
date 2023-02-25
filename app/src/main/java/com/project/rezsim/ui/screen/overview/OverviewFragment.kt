package com.project.rezsim.ui.screen.overview

import android.graphics.PorterDuff
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.rezsim.R
import com.project.rezsim.base.RezsimFragment
import com.project.rezsim.device.DrawableRepository
import com.project.rezsim.device.StringRepository
import com.project.rezsim.device.dp
import com.project.rezsim.ui.screen.activity.MainActivityViewModel
import com.project.rezsim.ui.screen.header.HeaderViewModel
import com.project.rezsim.ui.screen.main.MainFragment
import com.project.rezsim.ui.screen.overview.list.MeasurementAdapter
import com.project.rezsim.ui.view.message.MessageType
import org.koin.android.ext.android.inject

class OverviewFragment : RezsimFragment() {

    override val contentId: Int = R.layout.overview_fragment

    private val viewModel: OverviewViewModel by inject()
    private val activityViewModel: MainActivityViewModel by inject()
    private val headerViewModel: HeaderViewModel by inject()
    private val drawableRepository: DrawableRepository by inject()
    private val stringRepository: StringRepository by inject()

    private lateinit var yearButton: AppCompatButton
    private val monthSelectorButtons = mutableListOf<AppCompatButton>()
    private lateinit var measurementAdapter: MeasurementAdapter

    override fun setupViews() {
        super.setupViews()
        headerViewModel.setTitle(viewModel.title())
        view?.let {
            yearButton = it.findViewById<AppCompatButton>(R.id.btYear).apply {
                isSelected = true
            }
            val listView = it.findViewById<RecyclerView>(R.id.rvMeters).apply {
                layoutManager = LinearLayoutManager(requireContext())
                measurementAdapter = MeasurementAdapter()
                adapter = measurementAdapter
            }
        }
        refreshMonthSelector()
        monthSelectorButtons.last().performClick()
    }

    override fun subscribeObservers() {
        super.subscribeObservers()
        headerViewModel.backLiveData.observeForever {
            goBack(it)
        }

    }

    private fun refreshMonthSelector() {
        view?.let {
            val layout: LinearLayout = it.findViewById(R.id.llMonths)
            monthSelectorButtons.clear()
            layout.removeAllViews()
            viewModel.months().forEachIndexed { index, month ->
                val button = createMonthButton(index, month)
                monthSelectorButtons.add(button)
                layout.addView(button)
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
        setTextColor(resources.getColor(R.color.button_text_color_dark))
        textAlignment = View.TEXT_ALIGNMENT_CENTER
        setPadding(8.dp, 0, 8.dp, 0)
        backgroundTintList = resources.getColorStateList(R.color.material_grey_5, null)
        backgroundTintMode = PorterDuff.Mode.ADD
        isAllCaps = false
        setOnClickListener { v ->
            monthSelectorButtons.forEach {
                it.isSelected = it.id == v.id
            }
            yearButton.text = viewModel.months()[v.id].year.toString()
            measurementAdapter.setItems(viewModel.collectMeasurements(v.id))
        }
    }

    private fun goBack(value: Boolean) {
        if (value) {
            headerViewModel.clearBackLiveData()
            activityViewModel.switchToFragment(MainFragment.TAG)
        }
    }

    companion object {
        const val TAG = "OverviewFragment"
        fun newinstance() = OverviewFragment()
    }
}
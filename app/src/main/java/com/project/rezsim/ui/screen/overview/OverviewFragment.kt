package com.project.rezsim.ui.screen.overview

import android.graphics.PorterDuff
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import com.project.rezsim.R
import com.project.rezsim.base.RezsimFragment
import com.project.rezsim.device.DrawableRepository
import com.project.rezsim.device.StringRepository
import com.project.rezsim.device.dp
import com.project.rezsim.ui.screen.activity.MainActivityViewModel
import com.project.rezsim.ui.screen.header.HeaderViewModel
import com.project.rezsim.ui.screen.main.MainFragment
import com.project.rezsim.ui.view.message.MessageType
import org.koin.android.ext.android.inject

class OverviewFragment : RezsimFragment() {

    override val contentId: Int = R.layout.overview_fragment

    private val viewModel: OverviewViewModel by inject()
    private val activityViewModel: MainActivityViewModel by inject()
    private val headerViewModel: HeaderViewModel by inject()
    private val drawableRepository: DrawableRepository by inject()
    private val stringRepository: StringRepository by inject()

    private val monthSelectorButtons = mutableListOf<AppCompatButton>()

    override fun setupViews() {
        super.setupViews()
        headerViewModel.setTitle(viewModel.title())
        refreshMonthSelector()

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
            repeat(12) {
                val button = createMonthButton(it)
                monthSelectorButtons.add(button)
                layout.addView(button)
            }

        }
    }

    private fun createMonthButton(id: Int) = AppCompatButton(requireContext()).apply {
        setId(id)
        layoutParams =  ViewGroup.MarginLayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT).apply {
            setMargins(4.dp, 0, 4.dp, 0)
        }
        background = drawableRepository.getById(R.drawable.statelist_button_background)
        text = stringRepository.getByName("overview_month_$id")
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
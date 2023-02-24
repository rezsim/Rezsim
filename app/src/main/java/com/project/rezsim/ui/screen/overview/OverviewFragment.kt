package com.project.rezsim.ui.screen.overview

import android.graphics.PorterDuff
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import com.project.rezsim.R
import com.project.rezsim.base.RezsimFragment
import com.project.rezsim.device.DrawableRepository
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


    override fun setupViews() {
        super.setupViews()
        headerViewModel.setTitle(viewModel.title())

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
            layout.removeAllViews()
            repeat(12) {

            }

        }
    }

    private fun createMonthButton(id: Int) = AppCompatButton(requireContext()).apply {
        setId(id)
        layoutParams =  ViewGroup.MarginLayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT).apply {
            setMargins(0, 0, 8.dp, 0)
        }
        background = drawableRepository.getById(R.drawable.statelist_button_background)
        setText(text)
        setTextColor(resources.getColor(R.color.button_text_color_dark))
        textAlignment = View.TEXT_ALIGNMENT_CENTER
        setPadding(8.dp, 0, 8.dp, 0)
        backgroundTintList = resources.getColorStateList(R.color.material_grey_5, null)
        backgroundTintMode = PorterDuff.Mode.ADD
        isAllCaps = false
        setOnClickListener(householdsButtonClickListener)
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
package com.project.rezsim.ui.screen.overview

import com.project.rezsim.R
import com.project.rezsim.base.RezsimFragment
import org.koin.android.ext.android.inject

class OverviewFragment : RezsimFragment() {

    override val contentId: Int = R.layout.overview_fragment

    private val viewModel: OverviewViewModel by inject()

    companion object {
        const val TAG = "OverviewFragment"
        fun newinstance() = OverviewFragment()
    }
}
package com.project.rezsim.ui.screen.overview

import com.project.rezsim.R
import com.project.rezsim.base.RezsimFragment
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
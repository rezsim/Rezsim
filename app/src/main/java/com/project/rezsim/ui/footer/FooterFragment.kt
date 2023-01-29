package com.project.rezsim.ui.footer

import com.project.rezsim.R
import com.project.rezsim.base.RezsimFragment
import org.koin.android.ext.android.inject

class FooterFragment : RezsimFragment() {

    override val contentId = R.layout.footer_fragment

    private val viewModel: FooterViewModel by inject()

    companion object {
        const val TAG = "FooterFragment"
        fun newInstance() = FooterFragment()
    }
}
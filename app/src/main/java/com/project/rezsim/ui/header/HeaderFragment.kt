package com.project.rezsim.ui.header

import com.project.rezsim.R
import com.project.rezsim.base.RezsimFragment
import org.koin.android.ext.android.inject

class HeaderFragment : RezsimFragment() {

    override val contentId = R.layout.header_fragment

    private val viewModel: HeaderViewModel by inject()

    companion object {
        const val TAG = "HeaderFragment"
        fun newInstance() = HeaderFragment()
    }
}
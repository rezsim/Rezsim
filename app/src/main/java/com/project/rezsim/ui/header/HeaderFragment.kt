package com.project.rezsim.ui.header

import androidx.fragment.app.Fragment

class HeaderFragment : Fragment() {

    private lateinit var viewModel: HeaderViewModel

    companion object {
        const val TAG = "HeaderFragment"
        fun newInstance() = HeaderFragment()
    }
}
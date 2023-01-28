package com.project.rezsim.ui.footer

import androidx.fragment.app.Fragment

class FooterFragment : Fragment() {

    private lateinit var viewModel: FooterViewModel



    companion object {
        const val TAG = "FooterFragment"
        fun newInstance() = FooterFragment()
    }
}
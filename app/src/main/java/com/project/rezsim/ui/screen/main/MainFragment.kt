package com.project.rezsim.ui.screen.main

import com.project.rezsim.R
import com.project.rezsim.base.RezsimFragment

class MainFragment : RezsimFragment() {

    override val contentId = R.layout.main_fragment


    companion object {
        const val TAG = "MainFragment"
        fun newInstance() = MainFragment()
    }

}
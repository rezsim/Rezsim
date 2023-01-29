package com.project.rezsim.ui.household

import com.project.rezsim.R
import com.project.rezsim.base.RezsimFragment

class HouseholdFragment : RezsimFragment() {

    override val contentId = R.layout.household_fragment

    companion object {
        const val TAG = "HouseholdFragment"
        fun newInstance() = HouseholdFragment()
    }

}
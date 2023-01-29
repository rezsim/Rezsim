package com.project.rezsim.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.rezsim.R
import com.project.rezsim.base.RezsimFragment

class MainFragment : RezsimFragment() {

    override val contentId = R.layout.main_fragment


    companion object {
        const val TAG = "MainFragment"
        fun newInstance() = MainFragment()
    }

}
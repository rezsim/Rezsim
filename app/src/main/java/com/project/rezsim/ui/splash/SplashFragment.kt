package com.project.rezsim.ui.splash

import android.content.Context
import com.madhava.keyboard.vario.base.Singletons
import com.project.rezsim.R
import com.project.rezsim.base.RezsimFragment

class SplashFragment : RezsimFragment() {

    override val contentId = R.layout.splash_fragment

    private lateinit var viewModel: SplashViewModel

    override fun createViewModel() {
        viewModel = Singletons.instance(SplashViewModel::class) as SplashViewModel
        viewModel.load()
    }


    companion object {
        const val TAG = "SplashFragment"
        fun newInstance() = SplashFragment()
    }

}
package com.project.rezsim.ui.splash

import android.content.Context
import android.os.Bundle
import android.view.View
import com.madhava.keyboard.vario.base.Singletons
import com.project.rezsim.R
import com.project.rezsim.base.RezsimFragment
import com.project.server.UserModel
import org.koin.android.ext.android.inject

class SplashFragment : RezsimFragment() {

    override val contentId = R.layout.splash_fragment

    private val viewModel: SplashViewModel by inject()
    private val userModel: UserModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.load()
    }

    override fun subscribeObservers() {
        userModel.loggedInLiveData.observe(this) { viewModel.processFinished() }
    }

    companion object {
        const val TAG = "SplashFragment"
        fun newInstance() = SplashFragment()
    }

}
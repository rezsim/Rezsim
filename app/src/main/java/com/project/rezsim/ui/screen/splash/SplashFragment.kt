package com.project.rezsim.ui.screen.splash

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.project.rezsim.BuildConfig
import com.project.rezsim.R
import com.project.rezsim.base.RezsimFragment
import org.koin.android.ext.android.inject

class SplashFragment : RezsimFragment() {

    override val contentId = R.layout.splash_fragment

    private val viewModel: SplashViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.start()
    }

    override fun setupViews() {
        super.setupViews()
        view?.let {
            val ver = "v.${BuildConfig.VERSION_NAME}"
            it.findViewById<AppCompatTextView>(R.id.tvVersion).text = ver
        }
    }

    companion object {
        const val TAG = "SplashFragment"
        fun newInstance() = SplashFragment()
    }

}
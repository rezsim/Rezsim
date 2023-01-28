package com.project.rezsim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.project.rezsim.ui.MainActivityViewModel
import com.project.rezsim.ui.footer.FooterFragment
import com.project.rezsim.ui.header.HeaderFragment
import com.project.rezsim.ui.main.MainFragment
import com.project.rezsim.ui.splash.SplashFragment
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)


        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.loadWorkFragmentLiveData.observe(this) {setFragment(R.id.flWork, it)}
    }

    private fun setFragment(containerId: Int, fragmentTag: String) {
        findFragment(fragmentTag)?.let { fragment ->
            supportFragmentManager.beginTransaction()
                .replace(containerId, fragment, fragmentTag)
                .commitNow()
        }
    }

    private fun findFragment(tag: String): Fragment? =  supportFragmentManager.findFragmentByTag(tag)
            ?: when (tag) {
                HeaderFragment.TAG -> HeaderFragment.newInstance()
                FooterFragment.TAG -> FooterFragment.newInstance()
                SplashFragment.TAG -> SplashFragment.newInstance()
                else -> null
            }

}
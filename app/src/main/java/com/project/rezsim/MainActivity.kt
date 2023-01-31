package com.project.rezsim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.madhava.keyboard.vario.base.Singletons
import com.project.rezsim.ui.MainActivityViewModel
import com.project.rezsim.ui.footer.FooterFragment
import com.project.rezsim.ui.header.HeaderFragment
import com.project.rezsim.ui.household.HouseholdFragment
import com.project.rezsim.ui.login.LoginFragment
import com.project.rezsim.ui.main.MainFragment
import com.project.rezsim.ui.splash.SplashFragment
import org.koin.android.ext.android.inject
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by inject()

    private lateinit var progress: ContentLoadingProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        setupViews()
        subscribeObservers()
    }

    override fun onDestroy() {
        super.onDestroy()
        Singletons.clearAll()
        exitProcess(0)
    }

    private fun setupViews() {
        progress = findViewById(R.id.pbProgress)
        setFragment(R.id.flHeader, HeaderFragment.TAG)
        setFragment(R.id.flFooter, FooterFragment.TAG)
    }

    private fun subscribeObservers() {
        viewModel.loadWorkFragmentLiveData.observe(this) { setFragment(R.id.flWork, it) }
        viewModel.headerVisbileLiveData.observe(this) { setFragmentVisible(R.id.flHeader, it) }
        viewModel.footerVisbileLiveData.observe(this) { setFragmentVisible(R.id.flFooter, it) }
        viewModel.showProgressLiveData.observe(this) { showProgress(it) }
    }

    private fun setFragment(containerId: Int, fragmentTag: String) {
        Log.d("DEBINFO", "MainActivity.setFragment() fragmentTag:$fragmentTag")
        findFragment(fragmentTag)?.let { fragment ->
            supportFragmentManager.beginTransaction()
                .replace(containerId, fragment, fragmentTag)
                .commitNow()
        }
    }

    private fun findFragment(tag: String): Fragment? =  supportFragmentManager.findFragmentByTag(tag)
            ?: when (tag) {
                SplashFragment.TAG -> SplashFragment.newInstance()
                LoginFragment.TAG -> LoginFragment.newInstance()
                HeaderFragment.TAG -> HeaderFragment.newInstance()
                FooterFragment.TAG -> FooterFragment.newInstance()
                MainFragment.TAG -> MainFragment.newInstance()
                HouseholdFragment.TAG -> HouseholdFragment.newInstance()
                else -> null
            }

    private fun setFragmentVisible(containerId: Int, visible: Boolean) {
        findViewById<FrameLayout>(containerId).visibility = if (visible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun showProgress(visible: Boolean) {
        if (visible) {
            progress.show()
        } else {
            progress.hide()
        }
    }

}
package com.project.rezsim.ui.screen.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.madhava.keyboard.vario.base.Singletons
import com.project.rezsim.R
import com.project.rezsim.teszt.FragmentA
import com.project.rezsim.teszt.FragmentB
import com.project.rezsim.ui.screen.footer.FooterFragment
import com.project.rezsim.ui.screen.header.HeaderFragment
import com.project.rezsim.ui.screen.household.HouseholdFragment
import com.project.rezsim.ui.screen.login.LoginFragment
import com.project.rezsim.ui.screen.main.MainFragment
import com.project.rezsim.ui.screen.splash.SplashFragment
import com.project.rezsim.ui.view.message.Message
import com.project.rezsim.ui.view.message.MessageSeverity
import com.project.rezsim.ui.view.message.MessageType
import org.koin.android.ext.android.inject
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by inject()

    private lateinit var rootView: View
    private lateinit var progress: ContentLoadingProgressBar
    private lateinit var fab: FloatingActionButton



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
        rootView = findViewById(R.id.clRoot)
        progress = findViewById(R.id.pbProgress)
        fab = findViewById<FloatingActionButton>(R.id.fabFab).apply {
            setOnClickListener { fabPressed() }
        }
        setFragment(R.id.flHeader, HeaderFragment.TAG)
        setFragment(R.id.flFooter, FooterFragment.TAG)
    }

    private fun subscribeObservers() {
        viewModel.loadWorkFragmentLiveData.observe(this) { setFragment(R.id.flWork, it) }
        viewModel.headerVisbileLiveData.observe(this) { setFragmentVisible(R.id.flHeader, it) }
        viewModel.footerVisbileLiveData.observe(this) { setFragmentVisible(R.id.flFooter, it) }
        viewModel.showProgressLiveData.observe(this) { showProgress(it) }
        viewModel.fabIconLiveData.observe(this) { showFab(it) }
        viewModel.messageLiveData.observe(this) { showMessage(it) }
    }

    private fun setFragment(containerId: Int, fragmentTag: String) {
        Log.d("DEBINFO", "MainActivity.setFragment() fragmentTag:$fragmentTag")
        val fm = supportFragmentManager
        val tr = fm.beginTransaction()
        val currentFragment = fm.findFragmentById(containerId)
        currentFragment?.let {
            tr.detach(it)
        }
        val newFragment = fm.findFragmentByTag(fragmentTag)
        if (newFragment == null) {
           tr.add(containerId, createFragment(fragmentTag), fragmentTag)
        } else {
            tr.attach(newFragment)
        }
        tr.commit()
    }

    private fun createFragment(tag: String): Fragment =
            when (tag) {
                SplashFragment.TAG -> SplashFragment.newInstance()
                LoginFragment.TAG -> LoginFragment.newInstance()
                HeaderFragment.TAG -> HeaderFragment.newInstance()
                FooterFragment.TAG -> FooterFragment.newInstance()
                MainFragment.TAG -> MainFragment.newInstance()
                HouseholdFragment.TAG -> HouseholdFragment.newInstance()
                FragmentA.TAG -> FragmentA.newInstance()
                FragmentB.TAG -> FragmentB.newInstance()
                else -> error("Failed to create fragment $tag")
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

    private fun showFab(iconRes: Int?) {
        fab.visibility = if (iconRes != null) View.VISIBLE else View.GONE
        iconRes?.let {
            fab.setImageDrawable(AppCompatResources.getDrawable(this, iconRes))
        }
    }

    private fun showMessage(messageData: MessageData) {
        Message.show(this, rootView, messageData.message, messageData.messageType, messageData.severity, messageData.runnable)
    }

    private fun fabPressed() {
        viewModel.fabPressed()
    }

    data class MessageData(
        val message: String,
        val messageType: MessageType,
        val severity: MessageSeverity,
        val runnable: Runnable? = null
    )

}
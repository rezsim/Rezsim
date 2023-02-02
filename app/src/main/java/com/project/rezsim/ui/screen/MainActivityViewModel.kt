package com.project.rezsim.ui.screen

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.project.rezsim.base.RezsimViewModel
import com.project.rezsim.ui.screen.household.HouseholdFragment
import com.project.rezsim.ui.screen.login.LoginFragment
import com.project.rezsim.ui.screen.login.LoginViewModel
import com.project.rezsim.ui.screen.main.MainFragment
import com.project.rezsim.ui.screen.splash.SplashFragment
import com.project.rezsim.ui.screen.splash.SplashViewModel
import com.project.server.UserModel
import org.koin.core.component.inject

class MainActivityViewModel : RezsimViewModel() {

    val headerVisbileLiveData = MutableLiveData<Boolean>()
    val footerVisbileLiveData = MutableLiveData<Boolean>()
    val fabVisibleLiveData = MutableLiveData<Boolean>()
    val loadWorkFragmentLiveData = MutableLiveData<String>()
    val showProgressLiveData = MutableLiveData<Boolean>()
    val messageLiveData: MutableLiveData<Int> = MutableLiveData()

    private val splashViewModel: SplashViewModel by inject()
    private val loginViewModel: LoginViewModel by inject()
    private val userModel: UserModel by inject()

    private var currentFragmentTag: String = ""
        set(value) {
            field = value
            setWorkFragment(value)
        }

    init {
        Log.d("DEBINFO", "MainActivityViewModel.init")
        currentFragmentTag = SplashFragment.TAG
        splashViewModel.finishedLiveData.observeForever { splashFinished() }
        loginViewModel.finishedLiveData.observeForever { loginFinished() }
    }

    fun currentFragmentTag() = currentFragmentTag

    fun showProgress() = showProgressLiveData.postValue(true)

    fun hideProgress() = showProgressLiveData.postValue(false)

    fun showMessage(messageResId: Int) {
        messageLiveData.postValue(messageResId)
    }

    private fun setWorkFragment(fragmentTag: String) {
        loadWorkFragmentLiveData.value = fragmentTag
        headerVisbileLiveData.value = needHeader(fragmentTag)
        footerVisbileLiveData.value = needFooter(fragmentTag)
        fabVisibleLiveData.value = needFab(fragmentTag)
    }

    private fun splashFinished() {
        if (currentFragmentTag == SplashFragment.TAG) {
            currentFragmentTag = if (!userModel.isLoggedIn()) LoginFragment.TAG else startScreen()
        }
    }

    private fun loginFinished() {
        if (currentFragmentTag == LoginFragment.TAG) {
            currentFragmentTag = startScreen()
        }
    }

    private fun startScreen() = if (userModel.hasHousehold()) MainFragment.TAG else HouseholdFragment.TAG

    private fun needHeader(fragmentId: String) =
        fragmentId in listOf(MainFragment.TAG, HouseholdFragment.TAG)

    private fun needFooter(fragmentId: String) =
        fragmentId in listOf(MainFragment.TAG)

    private fun needFab(fragmentId: String) =
        fragmentId in listOf(HouseholdFragment.TAG)


}
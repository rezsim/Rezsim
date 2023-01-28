package com.project.rezsim.ui.splash

import androidx.lifecycle.MutableLiveData
import com.project.rezsim.base.RezsimViewModel
import com.project.rezsim.base.singleton.Singleton
import com.project.rezsim.tool.Timer
import com.project.rezsim.ui.MainActivityViewModel
import com.project.server.UserModel
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SplashViewModel : RezsimViewModel() {

    val loadedLiveData = MutableLiveData<Boolean>()

    private val userModel: UserModel by inject()
    private val mainActivityViewModel: MainActivityViewModel by inject()

    private var timerEnd = false

    fun load() {
        if (mainActivityViewModel.currentFragmentTag() != SplashFragment.TAG) {
            return
        }
        timerEnd = false
        Timer.runDelayed({
            timerEnd = true
            processFinished()
        }, 2000)
        if (userModel.loggedInLiveData.value != true) {
            userModel.login()
        }
    }

    fun processFinished() {
        if (userModel.loggedInLiveData.value == true && timerEnd) {
            loadedLiveData.postValue(true)
        }
    }

}
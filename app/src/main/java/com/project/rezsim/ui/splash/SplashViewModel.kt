package com.project.rezsim.ui.splash

import androidx.lifecycle.MutableLiveData
import com.project.rezsim.base.RezsimViewModel
import com.project.rezsim.tool.Timer
import com.project.rezsim.ui.MainActivityViewModel
import com.project.server.UserModel
import com.project.server.login.Login
import com.project.server.login.LoginResult
import org.koin.core.component.inject

class SplashViewModel : RezsimViewModel() {

    val finishedLiveData = MutableLiveData<LoginResult?>()

    private val userModel: UserModel by inject()
    private val mainActivityViewModel: MainActivityViewModel by inject()

    private var timerFinished = false
    private var loginFinished = false
    private var loginResult: LoginResult? = null

    fun start() {
        if (mainActivityViewModel.currentFragmentTag() != SplashFragment.TAG) {
            return
        }

        val login = Login()

        timerFinished = false
        Timer.runDelayed({
            timerFinished = true
            processFinished()
        }, 3000)

        loginResult = null
        if (!userModel.isLoggedIn() && userModel.hasLoginAuthenticationData()) {
            login.login(userModel.getEmail()!!, userModel.getPassword()!!).observeForever {
                loginResult = it
                userModel.setLoginResult(it)
                loginFinished = true
                processFinished()
            }
        } else {
            loginFinished = true
            processFinished()
        }
    }

    private fun processFinished() {
        if (loginFinished && timerFinished) {
            finishedLiveData.postValue(loginResult)
            mainActivityViewModel.hideProgress()
        } else if (timerFinished) {
            mainActivityViewModel.showProgress()
        }
    }

}
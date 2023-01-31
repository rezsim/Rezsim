package com.project.rezsim.ui.login

import androidx.lifecycle.MutableLiveData
import com.project.rezsim.R
import com.project.rezsim.base.RezsimViewModel
import com.project.rezsim.ui.MainActivityViewModel
import com.project.server.UserModel
import com.project.server.login.Login
import org.koin.core.component.inject

class LoginViewModel : RezsimViewModel() {

    val emailLiveData: MutableLiveData<String> = MutableLiveData()
    val passwordLiveData: MutableLiveData<String> = MutableLiveData()
    val loginButtonEnabledLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val inputEnabledLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val messageLiveData: MutableLiveData<Int> = MutableLiveData()

    private val userModel: UserModel by inject()
    private val mainActivityViewModel: MainActivityViewModel by inject()

    fun start() {
        emailLiveData.value = userModel.getEmail() ?: ""
        passwordLiveData.value = userModel.getPassword() ?: ""
    }

    fun inputChanged(email: String, password: String) {
        loginButtonEnabledLiveData.value = email.isNotBlank() && password.isNotBlank()
    }

    fun loginButtonClicked(email: String, password: String) {
        mainActivityViewModel.showProgress()
        loginButtonEnabledLiveData.value = false
        inputEnabledLiveData.value = false
        val login = Login()
        login.login(email, password).observeForever {
            mainActivityViewModel.hideProgress()
            userModel.setLoginResult(it)
            start()
            if (!it.isSuccessed()) {
                inputEnabledLiveData.value = true
                if (it.response?.httpResponse == 401) {
                    passwordLiveData.value = ""
                    messageLiveData.value = R.string.login_login_invalid_credidental
                } else {
                    messageLiveData.value = R.string.login_login_failed
                }
            } else {
                messageLiveData.value = R.string.login_login_success
            }
        }
    }

}
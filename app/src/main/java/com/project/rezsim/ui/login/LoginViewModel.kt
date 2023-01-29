package com.project.rezsim.ui.login

import androidx.lifecycle.MutableLiveData
import com.project.rezsim.base.RezsimViewModel
import com.project.server.UserModel
import org.koin.core.component.inject

class LoginViewModel : RezsimViewModel() {

    val emailLiveData: MutableLiveData<String> = MutableLiveData()
    val passwordLiveData: MutableLiveData<String> = MutableLiveData()
    val loginButtonEnabledLiveData: MutableLiveData<Boolean> = MutableLiveData()

    private val userModel: UserModel by inject()

    fun start() {
        emailLiveData.value = userModel.getEmail() ?: ""
        passwordLiveData.value = userModel.getPassword() ?: ""
    }

    fun inputChanged(email: String, password: String) {
        loginButtonEnabledLiveData.value = email.isNotBlank() && password.isNotBlank()
    }


}
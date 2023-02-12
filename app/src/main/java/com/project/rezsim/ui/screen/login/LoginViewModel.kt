package com.project.rezsim.ui.screen.login

import androidx.lifecycle.MutableLiveData
import com.project.rezsim.R
import com.project.rezsim.base.RezsimViewModel
import com.project.rezsim.ui.screen.activity.MainActivityViewModel
import com.project.rezsim.ui.view.message.MessageSeverity
import com.project.rezsim.server.UserModel
import com.project.rezsim.server.login.LoginRepository
import com.project.rezsim.server.login.LoginResponse
import com.project.rezsim.server.login.LoginResult
import com.project.rezsim.server.register.RegisterResponse
import org.koin.core.component.inject

class LoginViewModel : RezsimViewModel() {

    val finishedLiveData = MutableLiveData<LoginResult?>()
    val emailLiveData: MutableLiveData<String> = MutableLiveData()
    val passwordLiveData: MutableLiveData<String> = MutableLiveData()
    val passwordAgainLiveData: MutableLiveData<String> = MutableLiveData()
    val loginButtonEnabledLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val inputEnabledLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val modeLiveData: MutableLiveData<String> = MutableLiveData()

    private val userModel: UserModel by inject()
    private val loginRepository: LoginRepository by inject()
    private val mainActivityViewModel: MainActivityViewModel by inject()

    fun start() {
        switchToLoginMode()
    }

    fun isLoginMode() = modeLiveData.value == LoginFragment.LOGIN_MODE

    fun inputChanged(email: String, password: String, passwordAgain: String) {
        loginButtonEnabledLiveData.value = email.isNotBlank() && password.isNotBlank() && (isLoginMode() || passwordAgain.isNotBlank())
    }

    fun loginButtonClicked(email: String, password: String, passwordAgain: String) {
        if (!isLoginMode() && password != passwordAgain) {
            mainActivityViewModel.showMessage(messageResId = R.string.login_login_passwords_no_match, severity = MessageSeverity.ERROR)
            passwordAgainLiveData.value = ""
            return
        }
        mainActivityViewModel.showProgress()
        loginButtonEnabledLiveData.value = false
        inputEnabledLiveData.value = false
        loginRepository.login(email, password, !isLoginMode()).observeForever {
            mainActivityViewModel.hideProgress()
            userModel.setLoginResult(it)
            if (!it.isSuccessed()) {
                inputEnabledLiveData.value = true
                if (it.response?.httpResponse == LoginResponse.CODE_INVALID_CREDIDENTAL) {
                    switchToLoginMode()
                    mainActivityViewModel.showMessage(messageResId = R.string.login_login_invalid_credidental, severity = MessageSeverity.ERROR)
                } else if (it.response?.httpResponse == RegisterResponse.CODE_EMAIL_EXISTS) {
                    mainActivityViewModel.showMessage(messageResId = R.string.login_login_email_exists, severity = MessageSeverity.ERROR)
                    emailLiveData.value = ""
                } else {
                    mainActivityViewModel.showMessage(messageResId = R.string.login_login_failed, severity = MessageSeverity.ERROR)
                    loginButtonEnabledLiveData.value = true
                }
            } else {
                finishedLiveData.value = it
            }
        }
    }

    fun changeMode() {
        if (modeLiveData.value == LoginFragment.LOGIN_MODE) {
            switchToRegistrationMode()
        } else {
            switchToLoginMode()
        }
    }

    fun switchToRegistrationMode() {
        emailLiveData.value = ""
        passwordLiveData.value = ""
        modeLiveData.value = LoginFragment.REGISTRATION_MODE
    }

    fun switchToLoginMode() {
        emailLiveData.value = userModel.getEmail() ?: ""
        passwordLiveData.value = userModel.getPassword() ?: ""
        modeLiveData.value = LoginFragment.LOGIN_MODE
    }

}
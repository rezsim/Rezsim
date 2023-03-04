package com.project.rezsim.server

import androidx.lifecycle.MutableLiveData
import com.project.rezsim.device.SettingsRepository
import com.project.rezsim.server.dto.User
import com.project.rezsim.server.dto.measurement.Utility
import com.project.rezsim.server.login.LoginResult
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UserModel : KoinComponent {

    val logoutLiveData = MutableLiveData<Boolean>()

    private val settingsRepository: SettingsRepository by inject()

    private var user: User? = null

    private var email = settingsRepository.readUserEmail()
    private var password = settingsRepository.readUserPassword()

    private var token: String? = null

    fun hasLoginAuthenticationData() = email != null && password != null

    fun isLoggedIn() = token != null

    fun hasHousehold() = user?.householdList()?.isNotEmpty() ?: false

    fun hasMeasurement(householdIndex: Int, utility: Utility) = if (hasHousehold()) {
        user?.householdList()?.get(householdIndex)?.measurementList(utility)?.isNotEmpty() ?: false
    } else {
        false
    }

    fun getEmail() = email
    fun getPassword() = password
    fun getUser() = user

    fun setToken(token: String?) {
        this.token = if (token == null) null else "Bearer $token"
    }

    fun getToken() = token

    fun setLoginResult(loginResult: LoginResult) {
        email = loginResult.email.also {
            settingsRepository.writeUserEmail(it)
        }
        if (loginResult.response?.isSuccessed() == true) {
            password = loginResult.password.also {
                settingsRepository.writeUserPassword(it)
            }
            setToken(loginResult.response.token!!)
            user = loginResult.user
        } else {
            logout()
        }
    }

    fun logout(restart: Boolean = false) {
        user = null
        token = null
        password = null
        settingsRepository.clearPassword()
        if (restart) {
            logoutLiveData.postValue(true)
        }
    }

    fun updateUser(user: User) {
        this.user = user
    }

}
package com.project.rezsim.server

import com.project.rezsim.base.singleton.Singleton
import com.project.rezsim.device.SettingsRepository
import com.project.rezsim.server.dto.Household
import com.project.rezsim.server.dto.User
import com.project.rezsim.server.login.LoginResult
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UserModel : KoinComponent, Singleton {

    private val settingsRepository: SettingsRepository by inject()

    private var user: User? = null

    private var email = settingsRepository.readUserEmail()
    private var password = settingsRepository.readUserPassword()

    private var token: String? = null

    fun hasLoginAuthenticationData() = email != null && password != null

    fun isLoggedIn() = token != null

    fun hasHousehold() = user?.households?.isNotEmpty() ?: false

    fun getEmail() = email
    fun getPassword() = password
    fun getUser() = user

    fun setLoginResult(loginResult: LoginResult) {
        email = loginResult.email.also {
            settingsRepository.writeUserEmail(it)
        }
        if (loginResult.response?.isSuccessed() == true) {
            password = loginResult.password.also {
                settingsRepository.writeUserPassword(it)
            }
            token = loginResult.response.token
            user = loginResult.user
        } else {
            logout()
        }
    }

    fun logout() {
        token = null
    }

    fun updateHousehold(houseHold: Household) {
        getUser()?.let {
            val source = it.households.find { it.id == houseHold.id }
            if (source != null) {

            } else {

            }
        }
    }



}
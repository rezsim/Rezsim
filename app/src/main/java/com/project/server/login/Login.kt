package com.project.server.login

import androidx.lifecycle.MutableLiveData
import com.project.server.UserModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.Executors

class Login : KoinComponent {

    private val testEmail = "teszt@teszt.com"
    private val testPassword = "Alma1234"
    private val testToken = "teszt_token"

    private lateinit var resultLiveData: MutableLiveData<LoginResult>

    fun login(email: String, password: String): MutableLiveData<LoginResult> {
        resultLiveData = MutableLiveData()
        Executors.newSingleThreadExecutor().execute {
            Thread.sleep(2000)
            val response = if (email == testEmail && password == testPassword) {
                LoginResponse(200, testToken)
            } else {
                LoginResponse(401)
            }
            resultLiveData.postValue(LoginResult(response, email, password))
        }
        return resultLiveData
    }

}
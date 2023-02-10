package com.project.rezsim.server.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.project.rezsim.server.UserModel
import com.project.rezsim.server.api.ApiClient
import com.project.rezsim.server.api.ApiInterface
import com.project.rezsim.server.dto.Household
import com.project.rezsim.server.dto.Measurement
import com.project.rezsim.server.dto.User
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.Executors

class Login : KoinComponent {

    private val testEmail = "test@test.com"
    private val testPassword = "Alma1234"
    private val testToken = "teszt_token"

    private val userModel: UserModel by inject()

    private lateinit var resultLiveData: MutableLiveData<LoginResult>

    fun loginR(email: String, password: String): MutableLiveData<LoginResult> {
        resultLiveData = MutableLiveData()

        Executors.newSingleThreadExecutor().execute {
            val res = callLogin(email, password)
            if (res.isSuccessed()) {
                userModel.setToken(res.token!!)
                com.project.rezsim.server.user.User().getUserSync(userModel.getToken() ?: "")
            } else {
                resultLiveData.postValue(LoginResult(res, email, password, null))
            }
        }


        return resultLiveData
    }

    private fun callLogin(email: String, password: String) = try {
        val apiClient = ApiClient.getApiClient()
        val request = LoginRequest(email = email, password = password)
        val apiInterface = apiClient.create(ApiInterface::class.java)
        val response = apiInterface.login(request).execute()
        if (response.isSuccessful) {
            LoginResponse(response.code(), response.body()!!.token)
        } else {
            LoginResponse(response.code())
        }
    } catch (Ex:Exception){
        LoginResponse(null)
    }

    private fun callGetUser() = try {

    } catch (Ex:Exception){
        LoginResponse(null)
    }



}
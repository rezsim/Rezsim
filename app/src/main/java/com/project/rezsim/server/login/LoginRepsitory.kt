package com.project.rezsim.server.login

import androidx.lifecycle.MutableLiveData
import com.project.rezsim.server.UserModel
import com.project.rezsim.server.api.ApiClient
import com.project.rezsim.server.api.ApiInterface
import com.project.rezsim.server.register.RegisterRepository
import com.project.rezsim.server.user.UserRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.Executors

class LoginRepository : KoinComponent {

    private val userModel: UserModel by inject()
    private val userRepository: UserRepository by inject()
    private val registerRepository: RegisterRepository by inject()

    private lateinit var resultLiveData: MutableLiveData<LoginResult>

    fun login(email: String, password: String, registration: Boolean = false): MutableLiveData<LoginResult> {
        resultLiveData = MutableLiveData()

        Executors.newSingleThreadExecutor().execute {
            if (registration) {
                val em = registerRepository.registerSync(email, password)
                if (!em.isSuccessed()) {
                    resultLiveData.postValue(LoginResult(LoginResponse(em.httpResponse), email, password, null))
                    return@execute
                }
            }
            val res = callLogin(email, password)
            if (res.isSuccessed()) {
                userModel.setToken(res.token!!)
                val userResponse = userRepository.getUserSync(userModel.getToken() ?: "")
                if (userResponse?.user != null) {
                    resultLiveData.postValue(LoginResult(res, email, password, userResponse.user))
                } else {
                    resultLiveData.postValue(LoginResult(LoginResponse(null), email, password, null))
                }
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
    } catch (Ex:Exception) {
        LoginResponse(null)
    }

}
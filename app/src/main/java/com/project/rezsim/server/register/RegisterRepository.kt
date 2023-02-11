package com.project.rezsim.server.register

import com.project.rezsim.server.api.ApiClient
import com.project.rezsim.server.api.ApiInterface
import org.koin.core.component.KoinComponent

class RegisterRepository : KoinComponent {

    fun registerSync(email: String, password: String) = try {
        val apiClient = ApiClient.getApiClient()
        val apiInterface = apiClient.create(ApiInterface::class.java)
        val request = RegisterRequest(email = email, password = password)
        val response = apiInterface.register(request).execute()
        if (response.isSuccessful) {
            RegisterResponse(response.code(), response.body())
        } else {
            RegisterResponse(response.code())
        }
    } catch (e: Exception) {
        RegisterResponse(null)
    }

}
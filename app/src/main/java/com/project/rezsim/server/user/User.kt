package com.project.rezsim.server.user

import androidx.lifecycle.MutableLiveData
import com.project.rezsim.server.api.ApiClient
import com.project.rezsim.server.api.ApiInterface

class User {

    private lateinit var resultLiveData: MutableLiveData<User>

    fun getUserSync(token: String) = try {
        val apiClient = ApiClient.getApiClient()
        val apiInterface = apiClient.create(ApiInterface::class.java)
        apiInterface.getUsers("Bearer $token").execute()
    } catch (e: Exception) {
        null
    }

}
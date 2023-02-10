package com.project.rezsim.server.user

import androidx.lifecycle.MutableLiveData
import com.project.rezsim.server.api.ApiClient
import com.project.rezsim.server.api.ApiInterface

class User {

    private lateinit var resultLiveData: MutableLiveData<UserResponse>

    fun getUserSync(token: String) {
        val apiClient = ApiClient.getApiClient()
        val apiInterface = apiClient.create(ApiInterface::class.java)
        val reponse = apiInterface.getUsers("Bearer $token").execute()

        var deb = 0
        deb++

    }

}
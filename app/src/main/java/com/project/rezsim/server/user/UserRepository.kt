package com.project.rezsim.server.user

import androidx.lifecycle.MutableLiveData
import com.project.rezsim.base.ServerRepository
import com.project.rezsim.server.api.ApiClient
import com.project.rezsim.server.api.ApiInterface
import org.koin.core.component.KoinComponent
import java.util.concurrent.Executors

class UserRepository : ServerRepository() {

    private lateinit var resultLiveData: MutableLiveData<UserResponse>

    fun getUser(): MutableLiveData<UserResponse> {
        resultLiveData = MutableLiveData()

        Executors.newSingleThreadExecutor().execute {
            val res = getUserSync()
            resultLiveData.postValue(res)
        }

        return resultLiveData
    }

    fun getUserSync() = try {
        val apiClient = ApiClient.getApiClient()
        val apiInterface = apiClient.create(ApiInterface::class.java)
        val res = apiInterface.getUsers(token).execute()
        UserResponse(res.code(), res.body())
    } catch (e: Exception) {
        null
    }

}
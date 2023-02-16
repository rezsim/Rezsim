package com.project.rezsim.server.household

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.project.rezsim.server.api.ApiClient
import com.project.rezsim.server.api.ApiInterface
import com.project.rezsim.server.dto.household.Household
import org.koin.core.component.KoinComponent
import retrofit2.Call
import java.util.concurrent.Executors

class HouseholdRepository : KoinComponent {

    private lateinit var resultLiveData: MutableLiveData<Boolean>

    fun addNewHousehold(household: Household, token: String): MutableLiveData<Boolean> {
        resultLiveData = MutableLiveData()

        Executors.newSingleThreadExecutor().execute {
            val apiClient = ApiClient.getApiClient()
            val apiInterface = apiClient.create(ApiInterface::class.java)
            try {
                val response = apiInterface.addNewHousehold(household, token).execute()
                resultLiveData.postValue(response.isSuccessful)
            } catch (Ex:Exception) {
                resultLiveData.postValue(false)
            }
        }
        return resultLiveData
    }
}
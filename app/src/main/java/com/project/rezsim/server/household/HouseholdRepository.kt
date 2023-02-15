package com.project.rezsim.server.household

import androidx.lifecycle.MutableLiveData
import com.project.rezsim.server.api.ApiClient
import com.project.rezsim.server.api.ApiInterface
import com.project.rezsim.server.dto.household.Household
import org.koin.core.component.KoinComponent
import retrofit2.Call
import java.util.concurrent.Executors

class HouseholdRepository : KoinComponent {

    private lateinit var resultLiveData: MutableLiveData<Any?>

    fun addNewHousehold(household: Household, token: String): MutableLiveData<Any?> {
        resultLiveData = MutableLiveData()

        Executors.newSingleThreadExecutor().execute {
            val apiClient = ApiClient.getApiClient()
            val apiInterface = apiClient.create(ApiInterface::class.java)
            try {
                val response = apiInterface.addNewHousehold(household, token).execute()

                var b = 0
                b++
            } catch (Ex:Exception) {
                var d = 0
                d++
            }

            var e = 0
            e++

        }

        return resultLiveData
    }
}
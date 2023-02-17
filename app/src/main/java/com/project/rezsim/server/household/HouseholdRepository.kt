package com.project.rezsim.server.household

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.project.rezsim.server.api.ApiClient
import com.project.rezsim.server.api.ApiInterface
import com.project.rezsim.server.dto.household.Household
import com.project.rezsim.server.dto.measurement.Measurement
import com.project.rezsim.server.measurement.MeasurementRepository
import com.project.rezsim.server.user.UserRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Call
import java.util.concurrent.Executors

class HouseholdRepository : KoinComponent {

    private lateinit var resultLiveData: MutableLiveData<Boolean>

    private val userRepository: UserRepository by inject()
    private val measurementRepository: MeasurementRepository by inject()

    fun addNewHousehold(household: Household, token: String): MutableLiveData<Boolean> {
        resultLiveData = MutableLiveData()

        val measurements = mutableListOf<Measurement>().apply {
            household.measurements.forEach {
                add(it.copy())
            }
        }
        household.measurements = arrayOf()

        Executors.newSingleThreadExecutor().execute {
            val apiClient = ApiClient.getApiClient()
            val apiInterface = apiClient.create(ApiInterface::class.java)
            try {
                val response = apiInterface.addNewHousehold(household, token).execute()
                if (!response.isSuccessful) {
                    resultLiveData.postValue(false)
                    return@execute
                }

                val userResponse = userRepository.getUserSync(token)
                if (userResponse?.isSuccessed() != true || userResponse.user == null ) {
                    resultLiveData.postValue(false)
                    return@execute
                }
                val addedHouseholdId = userResponse.user.households.last().id
                var success = true
                measurements.forEach {
                    it.householdId = addedHouseholdId
                    success = measurementRepository.addNewMeasurementSync(it, token) && success
                }
                resultLiveData.postValue(success)
            } catch (Ex:Exception) {
                resultLiveData.postValue(false)
            }
        }
        return resultLiveData
    }

    fun updateHousehold(id: Long, household: Household, token: String): MutableLiveData<Boolean> {
        resultLiveData = MutableLiveData()

        Executors.newSingleThreadExecutor().execute {
            val apiClient = ApiClient.getApiClient()
            val apiInterface = apiClient.create(ApiInterface::class.java)
            try {
                val response = apiInterface.updateHousehold(id, household, token).execute()
                resultLiveData.postValue(response.isSuccessful)
            } catch (e: Exception) {
                resultLiveData.postValue(false)
            }
        }
        return resultLiveData
    }
}
package com.project.rezsim.server.measurement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.project.rezsim.base.ServerRepository
import com.project.rezsim.server.api.ApiClient
import com.project.rezsim.server.api.ApiInterface
import com.project.rezsim.server.dto.measurement.Measurement
import org.koin.core.component.KoinComponent
import java.lang.Exception
import java.util.concurrent.Executors

class MeasurementRepository : ServerRepository() {

    private lateinit var resultLiveData: MutableLiveData<Boolean>

    fun addNewMeasurement(measurement: Measurement): MutableLiveData<Boolean> {
        resultLiveData = MutableLiveData()
        Executors.newSingleThreadExecutor().execute {
            resultLiveData.postValue(addNewMeasurementSync(measurement))
        }
        return resultLiveData
    }

    fun addNewMeasurementSync(measurement: Measurement): Boolean = try {
        val apiClient = ApiClient.getApiClient()
        val apiInterface = apiClient.create(ApiInterface::class.java)
        val response = apiInterface.addNewMeasurement(measurement, token).execute()
        response.isSuccessful
    } catch (e: Exception) {
        false
    }





}
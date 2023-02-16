package com.project.rezsim.server.measurement

import com.project.rezsim.server.api.ApiClient
import com.project.rezsim.server.api.ApiInterface
import com.project.rezsim.server.dto.measurement.Measurement
import org.koin.core.component.KoinComponent
import java.lang.Exception

class MeasurementRepository : KoinComponent {

    fun addNewMeasurementSync(measurement: Measurement, token: String): Boolean = try {
        val apiClient = ApiClient.getApiClient()
        val apiInterface = apiClient.create(ApiInterface::class.java)
        val response = apiInterface.addNewMeasurement(measurement, token).execute()
        response.isSuccessful
    } catch (e: Exception) {
        false
    }





}
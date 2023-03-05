package com.project.rezsim.server.calculation

import androidx.lifecycle.MutableLiveData
import com.project.rezsim.base.ServerRepository
import com.project.rezsim.server.api.ApiClient
import com.project.rezsim.server.api.ApiInterface
import com.project.rezsim.server.dto.calculation.Calculation
import com.project.rezsim.ui.screen.overview.Month
import java.util.concurrent.Executors

class CalculationRepository : ServerRepository() {

    private lateinit var resultLiveData: MutableLiveData<Calculation?>

    fun getCalculation(householdId: Long, utility: Int, monthParam: Month?): MutableLiveData<Calculation?> {
        resultLiveData = MutableLiveData()

        Executors.newSingleThreadExecutor().execute {
            try {
                val apiClient = ApiClient.getApiClient()
                val apiInterface = apiClient.create(ApiInterface::class.java)
                val year = monthParam?.year?.toString() ?: ""
                val month = monthParam?.month?.let { it + 1 }?.toString() ?: ""
                val res = apiInterface.getCalculation(householdId, utility, year, month, token).execute()
                if (res.isSuccessful) {
                    resultLiveData.postValue(res.body())
                } else {
                    resultLiveData.postValue(null)
                }
            } catch (e: Exception) {
                resultLiveData.postValue(null)
            }
        }
        return resultLiveData
    }
}
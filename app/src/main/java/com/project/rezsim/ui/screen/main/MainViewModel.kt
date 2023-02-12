package com.project.rezsim.ui.screen.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.project.rezsim.R
import com.project.rezsim.base.RezsimViewModel
import com.project.rezsim.device.StringRepository
import com.project.rezsim.server.UserModel
import com.project.rezsim.server.dto.Household
import com.project.rezsim.server.dto.Measurement
import org.koin.core.component.inject

class MainViewModel : RezsimViewModel() {

    val addHouseholdLiveData = MutableLiveData<Boolean>()
    val electricityMeasurementLiveData = MutableLiveData<Measurement>()
    val gasMeasurementLiveData = MutableLiveData<Measurement>()

    private val stringRepository: StringRepository by inject()
    private val userModel: UserModel by inject()

    private var currentHousehold = 0

    fun householdItems(): List<String> = (userModel.getUser()?.households?.map { it.name }?.toMutableList() ?: mutableListOf()).apply {
        add(0, stringRepository.getById(R.string.main_spinner_household_prompt))
    }.toList()

    fun householdSelected(householdIndex: Int) {
        Log.d("DEBINFO-R", "householdIndex:$householdIndex")
        currentHousehold = householdIndex
        userModel.getUser()?.households?.get(currentHousehold)?.let {
            electricityMeasurementLiveData.value = it.measurements.last { true }
            gasMeasurementLiveData.value = it.measurements.last { true }
        }
    }

    fun getCurrentHousehold() = currentHousehold

}
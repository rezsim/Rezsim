package com.project.rezsim.ui.screen.main

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.project.rezsim.R
import com.project.rezsim.base.RezsimViewModel
import com.project.rezsim.device.SettingsRepository
import com.project.rezsim.device.StringRepository
import com.project.rezsim.server.UserModel
import com.project.rezsim.server.dto.measurement.Measurement
import com.project.rezsim.server.dto.measurement.Utility
import com.project.rezsim.ui.screen.activity.MainActivityViewModel
import com.project.rezsim.ui.screen.dialog.DialogParameter
import com.project.rezsim.ui.screen.dialog.meter.MeterDialogFragment
import org.koin.core.component.inject

class MainViewModel : RezsimViewModel() {

    val addHouseholdLiveData = MutableLiveData<Boolean>()
    val electricityMeasurementLiveData = MutableLiveData<Pair<Boolean, Measurement?>>()
    val gasMeasurementLiveData = MutableLiveData<Pair<Boolean, Measurement?>>()
    val refreshHouseholdsLiveData = MutableLiveData<Boolean>()

    private val stringRepository: StringRepository by inject()
    private val settingsRepository: SettingsRepository by inject()
    private val userModel: UserModel by inject()
    private val activityViewModel: MainActivityViewModel by inject()

    private var currentHousehold: Int? = null
    get() = field ?: settingsRepository.readLastHouseholdId().let { currentId ->
        var index = userModel.getUser()?.householdList()?.indexOfFirst { it.id == currentId } ?: 0
        if (index < 0) index = 0
        currentHousehold = index
        index
    }

    fun householdItems(): List<String> = (userModel.getUser()?.householdList()
        ?.map { it.name }?.toMutableList() ?: mutableListOf()).apply {
        add(0, stringRepository.getById(R.string.main_spinner_household_prompt))
    }.toList()

    fun householdSelected(householdIndex: Int) {
        currentHousehold = householdIndex
        userModel.getUser()?.householdList()?.get(currentHousehold!!)?.let {
            settingsRepository.writeLastHouseholdId(it.id)
            electricityMeasurementLiveData.value = Pair(it.electricityStatus == 1, it.lastMeasurement(Utility.ELECTRICITY_A))
            gasMeasurementLiveData.value = Pair(it.gasStatus == 1, it.lastMeasurement(Utility.GAS))
        }
    }

    fun currentHousehold(): Int = currentHousehold!!

    fun switchToLastHousehold() {
        currentHousehold = userModel.getUser()?.householdList()?.lastIndex ?: error("No household at switchToLastHousehold")
    }

    fun refresh() {
        refreshHouseholdsLiveData.value = true
    }

    fun readMeter(utility: Utility) {
        val meterDialogParam = Bundle().apply {
            putString(MeterDialogFragment.ARG_UTILITY, utility.name)
            putLong(MeterDialogFragment.ARG_HOUSEHOLD, userModel.getUser()?.householdList()?.get(currentHousehold!!)?.id ?: error("No household for read meter."))
        }
        activityViewModel.dialogLiveData.value = DialogParameter(MeterDialogFragment.TAG, meterDialogParam)
    }

}
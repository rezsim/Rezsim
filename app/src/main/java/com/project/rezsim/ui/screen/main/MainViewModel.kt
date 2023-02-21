package com.project.rezsim.ui.screen.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.madhava.keyboard.vario.base.Singletons
import com.project.rezsim.R
import com.project.rezsim.base.RezsimViewModel
import com.project.rezsim.device.StringRepository
import com.project.rezsim.server.UserModel
import com.project.rezsim.server.dto.measurement.Measurement
import com.project.rezsim.server.dto.measurement.Utility
import com.project.rezsim.server.user.UserRepository
import com.project.rezsim.ui.screen.activity.MainActivityViewModel
import com.project.rezsim.ui.screen.dialog.DialogParameter
import com.project.rezsim.ui.screen.dialog.meter.MeterDialogFragment
import com.project.rezsim.ui.screen.header.HeaderViewModel
import com.project.rezsim.ui.view.message.MessageSeverity
import com.project.rezsim.ui.view.message.MessageType
import org.koin.core.component.inject

class MainViewModel : RezsimViewModel() {

    val addHouseholdLiveData = MutableLiveData<Boolean>()
    val electricityMeasurementLiveData = MutableLiveData<Pair<Boolean, Measurement?>>()
    val gasMeasurementLiveData = MutableLiveData<Pair<Boolean, Measurement?>>()
    val refreshHouseholdsLiveData = MutableLiveData<Boolean>()

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
            electricityMeasurementLiveData.value = Pair(it.electricityStatus == 1, it.lastMeasurement(Utility.ELECTRICITY_A))
            gasMeasurementLiveData.value = Pair(it.gasStatus == 1, it.lastMeasurement(Utility.GAS))
        }
    }

    fun getCurrentHousehold() = currentHousehold

    fun switchToLastHousehold() {
        currentHousehold = userModel.getUser()?.households?.lastIndex ?: error("No household at switchToLastHousehold")
    }

    fun refresh() {
        refreshHouseholdsLiveData.value = true
    }

    fun readMeter(utility: Utility) {
        val meterDialogParam = Bundle().apply {
            putString(MeterDialogFragment.ARG_UTILITY, utility.name)
            putLong(MeterDialogFragment.ARG_HOUSEHOLD, userModel.getUser()?.households?.get(currentHousehold)?.id ?: error("No household for read meter."))
        }
        (Singletons.instance(MainActivityViewModel::class) as MainActivityViewModel).dialogLiveData.value = DialogParameter(MeterDialogFragment.TAG, meterDialogParam)
    }

}
package com.project.rezsim.ui.screen.main

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
import com.project.rezsim.ui.view.message.MessageSeverity
import com.project.rezsim.ui.view.message.MessageType
import org.koin.core.component.inject

class MainViewModel : RezsimViewModel() {

    val addHouseholdLiveData = MutableLiveData<Boolean>()
    val electricityMeasurementLiveData = MutableLiveData<Measurement?>()
    val gasMeasurementLiveData = MutableLiveData<Measurement?>()
    val refreshHouseholdsLiveData = MutableLiveData<Boolean>()

    private val stringRepository: StringRepository by inject()
    private val userModel: UserModel by inject()
    private val userRepository: UserRepository by inject()

    private var currentHousehold = 0

    fun householdItems(): List<String> = (userModel.getUser()?.households?.map { it.name }?.toMutableList() ?: mutableListOf()).apply {
        add(0, stringRepository.getById(R.string.main_spinner_household_prompt))
    }.toList()

    fun householdSelected(householdIndex: Int) {
        Log.d("DEBINFO-R", "householdIndex:$householdIndex")
        currentHousehold = householdIndex
        userModel.getUser()?.households?.get(currentHousehold)?.let {
            electricityMeasurementLiveData.value = it.measurements.lastOrNull { it.utility == Utility.ELECTRICITY_A.value }
            gasMeasurementLiveData.value = it.measurements.lastOrNull { it.utility == Utility.GAS.value }
        }
    }

    fun getCurrentHousehold() = currentHousehold

    fun setCurrentHousehold(household: Int) {
        currentHousehold = household
    }

    fun refresh() {
        MainActivityViewModel.getInstance().showProgress()
        userRepository.getUser(userModel.getToken()!!).observeForever {
            MainActivityViewModel.getInstance().hideProgress()
            if (it.isSuccessed()) {
                userModel.updateUser(it.user!!)
                householdSelected(currentHousehold)
                refreshHouseholdsLiveData.value = true
            } else {
                MainActivityViewModel.getInstance().showMessage(null, stringRepository.getById(R.string.main_message_unsuccesfull_refresh), MessageType.SNACKBAR_CLOSEABLE_AND_MANUALCLOSE, MessageSeverity.ERROR)
            }
        }
    }

}
package com.project.rezsim.ui.screen.dialog.meter

import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.project.rezsim.R
import com.project.rezsim.base.RezsimViewModel
import com.project.rezsim.device.StringRepository
import com.project.rezsim.server.UserModel
import com.project.rezsim.server.dto.measurement.Measurement
import com.project.rezsim.server.dto.measurement.Period
import com.project.rezsim.server.dto.measurement.Utility
import com.project.rezsim.server.measurement.MeasurementRepository
import com.project.rezsim.server.user.UserRepository
import com.project.rezsim.tool.DateHelper
import com.project.rezsim.ui.screen.activity.MainActivityViewModel
import com.project.rezsim.ui.screen.household.HouseholdFragment
import com.project.rezsim.ui.screen.main.MainFragment
import com.project.rezsim.ui.screen.main.MainViewModel
import com.project.rezsim.ui.view.message.MessageSeverity
import com.project.rezsim.ui.view.message.MessageType
import org.koin.core.component.inject
import java.util.*

class MeterDialogViewModel : RezsimViewModel() {

    val progressLiveData = MutableLiveData<Boolean>()
    val finishLiveData = MutableLiveData<Boolean>()

    lateinit var date: Calendar
    lateinit var utility: Utility
    var householdId: Long = -1

    private val stringRepository: StringRepository by inject()
    private val userModel: UserModel by inject()
    private val measurementRepository: MeasurementRepository by inject()
    private val userRepository: UserRepository by inject()
    private val activityViewModel: MainActivityViewModel by inject()
    private val mainViewModel: MainViewModel by inject()

    fun parseArguments(arg: Bundle?) {
        arg?.let {
            utility = Utility.valueOf(it.getString(MeterDialogFragment.ARG_UTILITY, ""))
            householdId = it.getLong(MeterDialogFragment.ARG_HOUSEHOLD)
            date = Calendar.getInstance(Locale.getDefault())
        }
    }

    fun message() = stringRepository.getById(if (utility == Utility.GAS) R.string.dialog_meter_read_utility_gas else R.string.dialog_meter_read_utility_electricity).let {
        stringRepository.getById(R.string.dialog_meter_read_message, it)
    }

    fun iconRes() = if (utility == Utility.GAS) R.drawable.ic_gas else R.drawable.ic_electricity

    fun lastValue() = userModel.getUser()
        ?.households?.find { it.id == householdId }
        ?.lastMeasurement(utility)
        ?.position

    fun save(position: Int, rootView: View) {
        if (position < lastValue() ?: 0) {
            activityViewModel.showMessage(null, R.string.dialog_meter_small_value_error, MessageType.SNACKBAR_CLOSEABLE_AND_MANUALCLOSE, MessageSeverity.ERROR, rootView)
            return
        }
        progressLiveData.value = true
        measurementRepository.addNewMeasurement(createMeasurement(position)).observeForever {
            if (it) {
                refreshUserAndGoBack()
            } else {
                progressLiveData.postValue(false)
                activityViewModel.showMessage(null, R.string.dialog_meter_message_unsuccesfull_save, MessageType.SNACKBAR_CLOSEABLE_AND_MANUALCLOSE, MessageSeverity.ERROR, rootView)
            }
        }
    }

    private fun createMeasurement(position: Int) = Measurement(
        id = -1,
        userId = userModel.getUser()!!.id,
        householdId = householdId,
        utility = utility.value,
        period = Period.DAILY.value,
        date = DateHelper.calendarToServerDateString(date),
        position = position,
        consumption = -1,
        level = -1
    )

    private fun refreshUserAndGoBack() {
        userRepository.getUser().observeForever {
            progressLiveData.postValue(false)
            if (!it.isSuccessed()) {
                activityViewModel.showMessage(null, stringRepository.getById(R.string.dialog_meter_message_unsuccesfull_refresh), MessageType.SNACKBAR_CLOSEABLE_AND_MANUALCLOSE, MessageSeverity.ERROR)
            } else {
                userModel.updateUser(it.user!!)
                finishLiveData.postValue(true)
                activityViewModel.switchToFragment(MainFragment.TAG)
                mainViewModel.refresh()
            }
        }
    }


}
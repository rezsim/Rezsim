package com.project.rezsim.ui.screen.overview

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.madhava.keyboard.vario.base.Singletons
import com.project.rezsim.R
import com.project.rezsim.base.RezsimViewModel
import com.project.rezsim.device.SettingsRepository
import com.project.rezsim.device.StringRepository
import com.project.rezsim.server.UserModel
import com.project.rezsim.server.dto.household.Household
import com.project.rezsim.server.dto.measurement.Measurement
import com.project.rezsim.server.dto.measurement.Utility
import com.project.rezsim.tool.DateHelper
import com.project.rezsim.ui.screen.activity.MainActivityViewModel
import com.project.rezsim.ui.screen.dialog.DialogParameter
import com.project.rezsim.ui.screen.dialog.meter.MeterDialogFragment
import com.project.rezsim.ui.screen.dialog.user.UserDialogFragment
import com.project.rezsim.ui.screen.header.HeaderViewModel
import org.koin.core.component.inject
import java.util.*

class OverviewViewModel : RezsimViewModel() {

    val monthSelectorVisibilityLiveData = MutableLiveData<Boolean>()
    val meterItemsLiveData = MutableLiveData<List<Measurement>>()

    private val userModel: UserModel by inject()
    private val headerViewModel: HeaderViewModel by inject()

    var householdIndex: Int = 0
    lateinit var utility: Utility

    private var months: List<Month>? = null
    private var currentMonth: Int = 0

    private val stringRepository: StringRepository by inject()
    private val settingsRepository: SettingsRepository by inject()

    fun init() {
        months = null
        setMonthSelectorVisibility(settingsRepository.readOverviewMonthSelectorVisible())
    }

    fun title() = stringRepository.getById(if (utility == Utility.GAS) R.string.overview_header_title_gas else R.string.overview_header_title_electricity)

    fun months() = months ?: calculateMonths().also {
        months = it
    }

    fun paymentBackground() = if (utility == Utility.GAS) R.color.gas_background else R.color.electricity_background

    fun headerButtonPressed(buttonId: Int) {
        if (buttonId == R.id.ivCalendar) {
            setMonthSelectorVisibility(!(monthSelectorVisibilityLiveData.value == true))
            settingsRepository.writeOverviewMonthSelectorVisible(monthSelectorVisibilityLiveData.value!!)
            meterItemsLiveData.value = collectMeasurements()
        }
    }

    fun selectMonth(monthIndex: Int) {
        currentMonth = monthIndex
        meterItemsLiveData.value = collectMeasurements()
    }

    fun minDate() = months().firstOrNull()?.startDate()?.let {
        DateHelper.serverDateStringToCalendar(it).time.time
    }

    fun readMeter() {
        val meterDialogParam = Bundle().apply {
            putString(MeterDialogFragment.ARG_UTILITY, utility.name)
            putLong(MeterDialogFragment.ARG_HOUSEHOLD, userModel.getUser()?.householdList()?.get(householdIndex)?.id ?: error("No household for read meter."))
        }
        (Singletons.instance(MainActivityViewModel::class) as MainActivityViewModel).dialogLiveData.value = DialogParameter(
            MeterDialogFragment.TAG, meterDialogParam)
    }

    fun refresh() {
        meterItemsLiveData.value = collectMeasurements()
    }

    private fun setMonthSelectorVisibility(visible: Boolean) {
        monthSelectorVisibilityLiveData.value = visible
        headerViewModel.setButtonColor(R.id.ivCalendar, if (visible) R.color.material_indigo_5 else R.color.material_grey_8)
    }

    private fun calculateMonths(): List<Month> = measurements().map {
        Log.d("DEBINFO-R", "calculateMonths()")
        val date = DateHelper.serverDateStringToCalendar(it.date)
        Month(date.get(Calendar.YEAR), date.get(Calendar.MONTH))
    }.distinct()

    private fun collectMeasurements(): List<Measurement> = if (monthSelectorVisibilityLiveData.value == false) {
        measurements()
    } else {
        val month = months()[currentMonth]
        val startDate = month.startDate()
        val endDate = month.endDate()
        measurements().filter { it.date in startDate..endDate }
    }.reversed()

    private fun measurements() = userModel.getUser()!!.householdList()[householdIndex].measurementList(utility)


}
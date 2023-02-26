package com.project.rezsim.ui.screen.overview

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.project.rezsim.R
import com.project.rezsim.base.RezsimViewModel
import com.project.rezsim.device.SettingsRepository
import com.project.rezsim.device.StringRepository
import com.project.rezsim.server.UserModel
import com.project.rezsim.server.dto.household.Household
import com.project.rezsim.server.dto.measurement.Utility
import com.project.rezsim.tool.DateHelper
import com.project.rezsim.ui.screen.dialog.DialogParameter
import com.project.rezsim.ui.screen.dialog.user.UserDialogFragment
import org.koin.core.component.inject
import java.util.*

class OverviewViewModel : RezsimViewModel() {

    val monthSelectorVisibilityLiveData = MutableLiveData<Boolean>()

    private val userModel: UserModel by inject()

    var householdIndex: Int = 0
    lateinit var utility: Utility

    private var months: List<Month>? = null

    private val stringRepository: StringRepository by inject()
    private val settingsRepository: SettingsRepository by inject()

    fun init() {
        months = null
        monthSelectorVisibilityLiveData.value = settingsRepository.readOverviewMonthSelectorVisible()
    }

    fun title() = stringRepository.getById(if (utility == Utility.GAS) R.string.overview_header_title_gas else R.string.overview_header_title_electricity)

    fun months() = months ?: calculateMonths().also {
        months = it
    }

    fun collectMeasurements(monthIndex: Int) = measurements()
        .filter { true }
        .sortedByDescending { it.date }

    fun paymentBackground() = if (utility == Utility.GAS) R.color.gas_background else R.color.electricity_background

    fun headerButtonPressed(buttonId: Int) {
        if (buttonId == R.id.ivCalendar) {
            monthSelectorVisibilityLiveData.value = !(monthSelectorVisibilityLiveData.value == true)
            settingsRepository.writeOverviewMonthSelectorVisible(monthSelectorVisibilityLiveData.value!!)
        }
    }

    private fun calculateMonths(): List<Month> = measurements().map {
        Log.d("DEBINFO-R", "calculateMonths()")
        val date = DateHelper.serverDateStringToCalendar(it.date)
        Month(date.get(Calendar.YEAR), date.get(Calendar.MONTH))
    }.distinct()

    private fun measurements() = userModel.getUser()!!.householdList()[householdIndex].measurementList(utility)


}
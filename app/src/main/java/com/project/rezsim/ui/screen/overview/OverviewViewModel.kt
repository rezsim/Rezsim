package com.project.rezsim.ui.screen.overview

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.project.rezsim.R
import com.project.rezsim.base.RezsimViewModel
import com.project.rezsim.device.SettingsRepository
import com.project.rezsim.device.StringRepository
import com.project.rezsim.server.UserModel
import com.project.rezsim.server.calculation.CalculationRepository
import com.project.rezsim.server.dto.calculation.Calculation
import com.project.rezsim.server.dto.measurement.Measurement
import com.project.rezsim.server.dto.measurement.Utility
import com.project.rezsim.tool.DateHelper
import com.project.rezsim.ui.screen.activity.MainActivityViewModel
import com.project.rezsim.ui.screen.dialog.DialogParameter
import com.project.rezsim.ui.screen.dialog.meter.MeterDialogFragment
import com.project.rezsim.ui.screen.header.HeaderViewModel
import org.koin.core.component.inject
import java.util.*

class OverviewViewModel : RezsimViewModel() {

    val monthSelectorVisibilityLiveData = MutableLiveData<Boolean>()
    val paymentVisibilityLiveData = MutableLiveData<Boolean>()
    val meterItemsLiveData = MutableLiveData<List<Measurement>>()
    val reinitLiveData = MutableLiveData<Boolean>()
    val calculationLiveData = MutableLiveData<Calculation?>()

    private val userModel: UserModel by inject()
    private val headerViewModel: HeaderViewModel by inject()
    private val calculationRepository: CalculationRepository by inject()
    private val activityViewModel: MainActivityViewModel by inject()

    var householdIndex: Int = 0
    lateinit var utility: Utility

    private var months: List<Month>? = null
    private var currentMonth: Int = 0

    private val stringRepository: StringRepository by inject()
    private val settingsRepository: SettingsRepository by inject()

    fun init() {
        months = null
        setMonthSelectorVisibility(settingsRepository.readOverviewMonthSelectorVisible())
        setPaymentVisibility(settingsRepository.readOverviewPaymentVisible())
        calculate()
    }

    fun title() = stringRepository.getById(if (utility == Utility.GAS) R.string.overview_header_title_gas else R.string.overview_header_title_electricity)

    fun months() = months ?: calculateMonths().also {
        months = it
    }

    fun paymentBackground() = if (utility == Utility.GAS) R.color.gas_background else R.color.electricity_background

    fun headerButtonPressed(buttonId: Int) {
        if (buttonId == R.drawable.ic_calendar) {
            setMonthSelectorVisibility(!(monthSelectorVisibilityLiveData.value == true))
            settingsRepository.writeOverviewMonthSelectorVisible(monthSelectorVisibilityLiveData.value!!)
            refresh()
        } else if (buttonId == R.drawable.ic_dollar) {
            setPaymentVisibility(!(paymentVisibilityLiveData.value == true))
            settingsRepository.writeOverviewPaymentVisible(paymentVisibilityLiveData.value!!)

        }
    }

    fun selectMonth(monthIndex: Int) {
        currentMonth = monthIndex
        refresh()
    }

    fun minDate() = months().firstOrNull()?.startDate()?.let {
        DateHelper.serverDateStringToCalendar(it).time.time
    }

    fun readMeter() {
        val meterDialogParam = Bundle().apply {
            putString(MeterDialogFragment.ARG_UTILITY, utility.name)
            putLong(MeterDialogFragment.ARG_HOUSEHOLD, userModel.getUser()?.householdList()?.get(householdIndex)?.id ?: error("No household for read meter."))
        }
        activityViewModel.dialogLiveData.value = DialogParameter(
            MeterDialogFragment.TAG, meterDialogParam)
    }

    fun refresh() {
        meterItemsLiveData.value = collectMeasurements()
        calculate()
    }

    fun reInit() {
        reinitLiveData.value = true
    }

    private fun setMonthSelectorVisibility(visible: Boolean) {
        monthSelectorVisibilityLiveData.value = visible
        headerViewModel.setButtonColor(R.drawable.ic_calendar, if (visible) R.color.material_indigo_5 else R.color.material_grey_8)
    }

    private fun setPaymentVisibility(visible: Boolean) {
        paymentVisibilityLiveData.value = visible
        headerViewModel.setButtonColor(R.drawable.ic_dollar, if (visible) R.color.material_indigo_5 else R.color.material_grey_8)
    }

    private fun calculateMonths(): List<Month> = measurements().map {
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

    private fun calculate() {
        activityViewModel.showProgress()
        val householdId = userModel.getUser()!!.householdList()[householdIndex].id
        val month = if (monthSelectorVisibilityLiveData.value == true) months?.get(currentMonth) else null
        calculationRepository.getCalculation(householdId, utility.value, month).observeForever {
            activityViewModel.hideProgress()
            calculationLiveData.postValue(it)
        }
    }

}
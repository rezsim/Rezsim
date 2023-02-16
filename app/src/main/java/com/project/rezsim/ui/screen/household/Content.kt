package com.project.rezsim.ui.screen.household

import com.project.rezsim.server.dto.household.Household
import com.project.rezsim.server.dto.measurement.Measurement
import com.project.rezsim.server.dto.measurement.Period
import com.project.rezsim.server.dto.measurement.Utility
import com.project.rezsim.tool.DateHelper

data class Content(
    val name: String,
    val electricityMeter: Int,
    val gasMeter: Int,
    val electricityUseMode: Int,
    val electricityPricingA: Int,
    val electricityPricingB: Int,
    val gasHeating: Int,
    val gasChildren: Int,
    val hasElectricity: Boolean,
    val hasGas: Boolean
) {

    fun applyOnHousehold(household: Household) {
        household.name = name
        household.electricityUsage = electricityUseMode
        household.electricityPricingTypeA = electricityPricingA
        household.electricityPricingTypeB = electricityPricingB
        household.gasHeatingValue = gasHeating
        household.gasChildren = gasChildren
        val measuremenList = household.measurements.toMutableList()
        if (electricityMeter != -1) {
            measuremenList.add(createMeasurement(Utility.ELECTRICITY_A.value, electricityMeter, household))
        }
        if (gasMeter != -1) {
            measuremenList.add(createMeasurement(Utility.GAS.value, gasMeter, household))
        }
        household.measurements = measuremenList.toTypedArray()
    }

    private fun createMeasurement(utility: Int, value: Int, household: Household) = Measurement(
        id = -1,
        userId = household.userId,
        householdId = household.id,
        utility = utility,
        period = Period.DAILY.value,
        date = DateHelper.calendarToServerDateString(DateHelper.now()),
        position = value,
        consumption = -1,
        level = -1
    )

    companion object {
        fun fromHousehold(household: Household): Content = Content(
            name = household.name,
            electricityMeter = -1,
            gasMeter = -1,
            electricityUseMode = household.electricityUsage,
            electricityPricingA = household.electricityPricingTypeA,
            electricityPricingB = household.electricityPricingTypeB,
            gasHeating = household.gasHeatingValue,
            gasChildren = household.gasChildren,
            hasElectricity = true,
            hasGas = true
//            hasElectricity = household.electricityStatus == 1,
//            hasGas = household.gasStatus == 1
        )
    }
}

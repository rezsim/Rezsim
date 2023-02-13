package com.project.rezsim.ui.screen.household

import com.project.rezsim.server.UserModel
import com.project.rezsim.server.dto.Household
import com.project.rezsim.server.dto.Measurement
import com.project.rezsim.server.dto.Utility
import com.project.rezsim.tool.DateHelper
import org.koin.core.component.KoinComponent

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
        if (electricityMeter != -1) {
            household.measurements.toMutableList().add(createMeasurement(Utility.ELECTRICITY_A.value, electricityMeter, household))
        }
        if (gasMeter != -1) {
            household.measurements.toMutableList().add(createMeasurement(Utility.GAS.value, gasMeter, household))
        }
    }

    private fun createMeasurement(utility: Int, value: Int, household: Household) = Measurement(
        id = -1,
        userId = household.userId,
        householdId = household.id,
        utility = utility,
        period = 0,
        date = DateHelper.calendarToServerString(DateHelper.now()),
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
            hasElectricity = household.electricityStatus == 1,
            hasGas = household.gasStatus == 1
        )
    }
}

package com.project.rezsim.ui.screen.household

import com.project.rezsim.server.dto.Household

data class Content(
    val name: String,
    val electricityMeter: Int,
    val gasMeter: Int,
    val electricityUseMode: Int,
    val electricityPricingA: Int,
    val electricityPricingB: Int,
    val gasHeating: Int,
    val gasChildren: Int
) {

    fun applyOnHousehold(household: Household) {
        household.name = name
        household.electricityUsage = electricityUseMode
        household.electricityPricingTypeA = electricityPricingA
        household.electricityPricingTypeB = electricityPricingB
        household.gasHeatingValue = gasHeating.toInt()
        household.gasChildren = gasChildren
        if (electricityMeter != -1) {
            // villany leolvasás hozzáadás
        }
        if (gasMeter != -1) {
            // gáz leolvasás hozzáadás
        }
    }

    companion object {
        fun fromHousehold(household: Household): Content = Content(
            name = household.name,
            electricityMeter = -1,
            gasMeter = -1,
            electricityUseMode = household.electricityUsage,
            electricityPricingA = household.electricityPricingTypeA,
            electricityPricingB = household.electricityPricingTypeB,
            gasHeating = household.gasHeatingValue,
            gasChildren = household.gasChildren
        )
    }
}

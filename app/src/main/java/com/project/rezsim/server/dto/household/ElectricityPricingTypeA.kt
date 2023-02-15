package com.project.rezsim.server.dto.household

import com.project.rezsim.R

enum class ElectricityPricingTypeA(val value: Int, val textResId: Int) {
    UNDEFINED(-1, R.string.household_item_select),
    GENERAL(0, R.string.household_pricing_mode_a_0)
}
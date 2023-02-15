package com.project.rezsim.server.dto.household

import com.project.rezsim.R

enum class ElectricityPricingTypeB(val value: Int, val textResId: Int) {
    UNDEFINED(-1, R.string.household_item_select),
    PERIODIC(0, R.string.household_pricing_mode_b_0)
}
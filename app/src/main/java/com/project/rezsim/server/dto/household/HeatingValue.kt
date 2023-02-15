package com.project.rezsim.server.dto.household

import com.project.rezsim.R

enum class HeatingValue(val value: Int, val textResId: Int) {
    UNDEFINED(-1, R.string.household_item_select),
    GENERAL(0, R.string.household_gas_heating_0),
    HEATING_1(1, R.string.household_gas_heating_1),
    HEATING_2(2, R.string.household_gas_heating_2),
    HEATING_3(3, R.string.household_gas_heating_3),
    HEATING_4(4, R.string.household_gas_heating_4),
    HEATING_5(5, R.string.household_gas_heating_5),

}
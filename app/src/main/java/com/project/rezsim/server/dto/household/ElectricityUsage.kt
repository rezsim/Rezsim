package com.project.rezsim.server.dto.household

import com.project.rezsim.R

enum class ElectricityUsage(val value: Int, val textResId: Int) {
    UNDEFINED(-1, R.string.household_item_select),
    RESIDENTAL(0, R.string.household_usage_mode_0)
}
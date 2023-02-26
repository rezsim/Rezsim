package com.project.rezsim.ui.screen.overview

import com.project.rezsim.tool.DateHelper

data class Month(
    val year: Int,
    val month: Int
) {
    fun startDate(): String = DateHelper.calendarToServerDateString(DateHelper.firstDayOf(year, month - 1))

    fun endDate(): String = DateHelper.calendarToServerDateString(DateHelper.lastDayOf(year, month - 1))

}

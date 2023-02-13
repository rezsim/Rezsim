package com.project.rezsim.tool

import java.text.SimpleDateFormat
import java.util.*

object DateHelper {

    private val serverFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSSS")

    fun now(): Calendar = Calendar.getInstance(Locale.getDefault())

    fun calendarToServerString(calendar: Calendar) = serverFormatter.format(calendar.time)


}
package com.project.rezsim.tool

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object DateHelper {

    private val serverDateTimeFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSSS")
    private val serverDateFormatter = SimpleDateFormat("yyyy-MM-dd")
    private val displayDateFormatter = SimpleDateFormat("yyyy. MMM d")

    fun now(): Calendar = Calendar.getInstance(Locale.getDefault())

    fun today(): Calendar = Calendar.getInstance(Locale.getDefault()).apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    fun calendarToServerDateTimeString(calendar: Calendar) = serverDateTimeFormatter.format(calendar.time)

    fun calendarToServerDateString(calendar: Calendar) = serverDateFormatter.format(calendar.time)

    fun calendarToDisplayDateString(calendar: Calendar) = displayDateFormatter.format(calendar.time)

    fun displayDateStringToCalendar(dateString: String) = Calendar.getInstance(Locale.getDefault()).apply {
        time = displayDateFormatter.parse(dateString) ?: error("Invalid date string: $dateString")
    }

    fun serverDateStringToCalendar(dateString: String) = Calendar.getInstance(Locale.getDefault()).apply {
        time = serverDateFormatter.parse(dateString) ?: error("Invalid date string: $dateString")
    }

    fun lastDayOf(year: Int, month: Int) = firstDayOf(year, month).apply {
        add(Calendar.MONTH, 1)
        add(Calendar.DAY_OF_YEAR, -1)
    }

    fun firstDayOf(year: Int, month: Int) = today().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month)
        set(Calendar.DAY_OF_MONTH, getActualMinimum(Calendar.DAY_OF_MONTH))
    }

}
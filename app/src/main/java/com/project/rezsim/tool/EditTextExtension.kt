package com.project.rezsim.tool

import androidx.appcompat.widget.AppCompatEditText

fun AppCompatEditText.numericValue(): Int? = text?.toString().let {
    if (it.isNullOrBlank()) null else try {
        it.toInt()
    } catch (e: Exception) {
        null
    }
}
package com.project.rezsim.device

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import com.project.rezsim.R
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ColorRepository : KoinComponent {

    private val context: Context by inject()

    fun drawable(colorResId: Int) = ColorDrawable(colorResId)

    fun stateList(colorResId: Int) = ContextCompat.getColorStateList(context, colorResId)

    fun color(colorResId: Int) = ContextCompat.getColor(context, colorResId)

}
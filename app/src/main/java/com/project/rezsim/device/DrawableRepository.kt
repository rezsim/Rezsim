package com.project.rezsim.device

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DrawableRepository : KoinComponent {

    private val context: Context by inject()

    fun getById(resId: Int) =  AppCompatResources.getDrawable(context, resId)
}
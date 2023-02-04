package com.project.rezsim.device

import android.content.Context
import com.project.rezsim.R
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.java.KoinJavaComponent.inject

class ScreenRepository : KoinComponent {

    private val context: Context by inject()

    fun isTablet() = context.resources.getBoolean(R.bool.is_tablet)

}
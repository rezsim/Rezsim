package com.project.rezsim.base

import android.content.Context
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class RezsimViewModel : KoinComponent {

    protected val context: Context by inject()

}
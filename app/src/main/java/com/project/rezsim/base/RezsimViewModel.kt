package com.project.rezsim.base

import android.content.Context
import com.project.rezsim.base.singleton.Singleton
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class RezsimViewModel : KoinComponent, Singleton {

    protected val context: Context by inject()

}
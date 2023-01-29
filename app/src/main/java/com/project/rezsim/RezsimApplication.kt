package com.project.rezsim

import android.app.Application
import com.project.rezsim.di.deviceModule
import com.project.rezsim.di.serverModule
import com.project.rezsim.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class RezsimApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@RezsimApplication)
            modules(listOf(viewModelModule, serverModule, deviceModule))
        }
    }
}
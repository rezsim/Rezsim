package com.project.rezsim.ui.splash

import androidx.lifecycle.MutableLiveData
import com.project.rezsim.base.RezsimViewModel
import com.project.rezsim.base.singleton.Singleton
import com.project.rezsim.tool.Timer
import org.koin.core.component.KoinComponent

class SplashViewModel : RezsimViewModel() {

    val loadedLiveData = MutableLiveData<Boolean>()

    fun load() {
        Timer.runDelayed({ loaded() }, 2000)
    }

    private fun loaded() {
        loadedLiveData.postValue(true)
    }

}
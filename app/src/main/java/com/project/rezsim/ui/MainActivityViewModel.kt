package com.project.rezsim.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.project.rezsim.base.RezsimViewModel
import com.project.rezsim.ui.splash.SplashFragment

class MainActivityViewModel : RezsimViewModel() {

    val headerVisbileLiveData = MutableLiveData<Boolean>()
    val footerVisbileLiveData = MutableLiveData<Boolean>()
    val loadWorkFragmentLiveData = MutableLiveData<String>()

    private var currentFragmentTag: String = ""
        set(value) {
            field = value
            setWorkFragment(value)
        }

    init {
        Log.d("DEBINFO", "MainActivityViewModel.init")
        currentFragmentTag = SplashFragment.TAG
    }

    fun currentFragmentTag() = currentFragmentTag

    private fun setWorkFragment(fragmentTag: String) {
        loadWorkFragmentLiveData.value = currentFragmentTag
        headerVisbileLiveData.value = fragmentTag != SplashFragment.TAG
        footerVisbileLiveData.value = fragmentTag != SplashFragment.TAG
    }

}
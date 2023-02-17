package com.project.rezsim.ui.screen.header

import androidx.lifecycle.MutableLiveData
import com.project.rezsim.base.RezsimViewModel

class HeaderViewModel : RezsimViewModel() {

    val userLiveData = MutableLiveData<Boolean>()
    val backLiveData = MutableLiveData<Boolean>()

    fun onBackPressed() {
        backLiveData.value = true
    }

    fun onUserPressed() {
        userLiveData.value = true
    }

    fun clearBackLveData() {
        backLiveData.value = false
    }


}
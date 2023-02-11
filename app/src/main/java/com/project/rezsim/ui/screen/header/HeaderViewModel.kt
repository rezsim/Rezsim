package com.project.rezsim.ui.screen.header

import androidx.lifecycle.MutableLiveData
import com.project.rezsim.base.RezsimViewModel

class HeaderViewModel : RezsimViewModel() {

    val userLiveData = MutableLiveData<Boolean>()

    fun onBackPressed() {

    }

    fun onUserPressed() {
        userLiveData.value = true
    }



}
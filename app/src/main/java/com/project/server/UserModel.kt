package com.project.server

import androidx.lifecycle.MutableLiveData
import com.project.rezsim.base.singleton.Singleton
import com.project.rezsim.tool.Timer
import org.koin.core.component.KoinComponent

class UserModel : KoinComponent, Singleton {

    val loggedInLiveData = MutableLiveData<Boolean>()

    fun login() {
        Timer.runDelayed({ loggedInLiveData.postValue(true) }, 3000)
    }

}
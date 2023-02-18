package com.project.rezsim.ui.screen.header

import androidx.lifecycle.MutableLiveData
import com.project.rezsim.base.RezsimViewModel
import com.project.rezsim.device.StringRepository
import org.koin.core.component.inject

class HeaderViewModel : RezsimViewModel() {

    private val stringRepository: StringRepository by inject()

    val userLiveData = MutableLiveData<Boolean>()
    val backLiveData = MutableLiveData<Boolean>()
    val titleLiveData = MutableLiveData<String>()

    fun onBackPressed() {
        backLiveData.value = true
    }

    fun onUserPressed() {
        userLiveData.value = true
    }

    fun clearBackLiveData() {
        backLiveData.value = false
    }

    fun setTitle(titleTextResId: Int?) {
        setTitle(titleTextResId?.let { stringRepository.getById(it) })
    }

    fun setTitle(tilteText: String?) {
        titleLiveData.value = tilteText ?: ""
    }


}
package com.project.rezsim.ui.screen.header

import androidx.lifecycle.MutableLiveData
import com.project.rezsim.base.RezsimViewModel
import com.project.rezsim.device.StringRepository
import org.koin.core.component.inject

class HeaderViewModel : RezsimViewModel() {

    val buttonPressedLiveData = MutableLiveData<Int>()
    val buttonColorLiveData = MutableLiveData<Pair<Int, Int>>()
    val backLiveData = MutableLiveData<Boolean>()
    val titleLiveData = MutableLiveData<String>()
    val toolButtonsLiveData = MutableLiveData<IntArray>()

    private val stringRepository: StringRepository by inject()

    fun onBackPressed() {
        backLiveData.value = true
    }

    fun onToolButtonPressed(buttonId: Int) {
        buttonPressedLiveData.value = buttonId
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

    fun setToolButtons(buttonsResId: IntArray?) {
        toolButtonsLiveData.value = buttonsResId ?: intArrayOf()
    }

    fun setButtonColor(buttonResId: Int, colorResId: Int) {
        buttonColorLiveData.value = Pair(buttonResId, colorResId)
    }


}
package com.project.rezsim.ui.screen.footer

import androidx.lifecycle.MutableLiveData
import com.project.rezsim.base.RezsimViewModel

class FooterViewModel : RezsimViewModel() {

    val buttonSelectedLiveData = MutableLiveData<FooterButton>()
    val selectButtonLiveData = MutableLiveData<FooterButton>()

    fun selectButton(button: FooterButton) {
        buttonSelectedLiveData.value = button
    }

    fun onButtonPressed(button: FooterButton) {
        if (buttonSelectedLiveData.value != button) {
            selectButton(button)
            selectButtonLiveData.value = button
        }
    }

}
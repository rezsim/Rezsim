package com.project.rezsim.ui.screen.dialog.user

import com.project.rezsim.base.RezsimViewModel
import com.project.rezsim.server.UserModel
import org.koin.core.component.inject

class UserDialogViewModel : RezsimViewModel() {

    private val userModel: UserModel by inject()

    fun user() = userModel.getEmail()

    fun logoutClicked() {
        userModel.logout(restart = true)

    }

}
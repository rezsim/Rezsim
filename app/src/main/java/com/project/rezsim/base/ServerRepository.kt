package com.project.rezsim.base

import com.project.rezsim.server.UserModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class ServerRepository : KoinComponent {

    protected val userModel: UserModel by inject()

    protected val token: String
        get() = userModel.getToken() ?: error("Call server repository before success login.")

}
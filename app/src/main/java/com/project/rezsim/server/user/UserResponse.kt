package com.project.rezsim.server.user

import com.project.rezsim.server.dto.User

data class UserResponse(
    val httpResponse: Int?,
    val items: Array<User>?
)

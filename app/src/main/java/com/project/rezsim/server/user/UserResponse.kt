package com.project.rezsim.server.user

import com.project.rezsim.server.dto.User

data class UserResponse(
    val httpResponse: Int?,
    val user: User?
) {
    fun isSuccessed() = httpResponse == CODE_OK && user != null

    companion object {
        const val CODE_OK = 200
    }

}

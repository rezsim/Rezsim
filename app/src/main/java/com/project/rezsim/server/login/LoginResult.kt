package com.project.rezsim.server.login

import com.project.rezsim.server.dto.User

data class LoginResult(
    val response: LoginResponse?,
    val email: String,
    val password: String,
    val user: User? = null
) {
    fun isSuccessed() = response?.isSuccessed() == true
}

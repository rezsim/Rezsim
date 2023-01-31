package com.project.server.login

import com.project.server.dto.User

data class LoginResult(
    val response: LoginResponse?,
    val email: String,
    val password: String,
    val user: User
) {
    fun isSuccessed() = response?.isSuccessed() == true
}

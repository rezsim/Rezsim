package com.project.server.login

data class LoginResult(
    val response: LoginResponse?,
    val email: String,
    val password: String
)

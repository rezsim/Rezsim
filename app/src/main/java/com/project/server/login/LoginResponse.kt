package com.project.server.login

data class LoginResponse(
    val httpResponse: Int,
    val token: String? = null
) {
    fun isSuccessed() = httpResponse == 200 && token != null
}

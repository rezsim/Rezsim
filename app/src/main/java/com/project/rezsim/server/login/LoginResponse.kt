package com.project.rezsim.server.login

data class LoginResponse(
    val httpResponse: Int?,
    val token: String? = null
) {
    fun isSuccessed() = httpResponse == CODE_OK && token != null

    companion object {
        const val CODE_OK = 200
        const val CODE_INVALID_CREDIDENTAL = 401
    }

}

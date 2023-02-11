package com.project.rezsim.server.register

data class RegisterResponse(
    val httpResponse: Int?,
    val email: String? = null
) {
    fun isSuccessed() = httpResponse == CODE_OK && email != null

    companion object {
        const val CODE_OK = 200
        const val CODE_EMAIL_EXISTS = 400
    }
}

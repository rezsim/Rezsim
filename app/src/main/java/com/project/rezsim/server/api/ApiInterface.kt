package com.project.rezsim.server.api

import com.project.rezsim.server.login.LoginRequest
import com.project.rezsim.server.login.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {
    @POST("login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}
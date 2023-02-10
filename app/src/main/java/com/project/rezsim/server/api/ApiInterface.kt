package com.project.rezsim.server.api

import com.project.rezsim.server.login.LoginRequest
import com.project.rezsim.server.login.LoginResponse
import com.project.rezsim.server.user.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiInterface {
    @POST("login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("api/user")
    fun getUsers(@Header("Authorization") token: String): Call<UserResponse>
}
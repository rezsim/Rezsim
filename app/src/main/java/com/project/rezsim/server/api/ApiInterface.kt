package com.project.rezsim.server.api

import com.project.rezsim.server.dto.User
import com.project.rezsim.server.dto.calculation.Calculation
import com.project.rezsim.server.dto.household.Household
import com.project.rezsim.server.dto.measurement.Measurement
import com.project.rezsim.server.login.LoginRequest
import com.project.rezsim.server.login.LoginResponse
import com.project.rezsim.server.register.RegisterRequest
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {
    @POST("register")
    fun register(@Body request: RegisterRequest): Call<String>

    @POST("login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("api/user")
    fun getUsers(@Header("Authorization") token: String): Call<User?>

    @POST("api/household")
    fun addNewHousehold(@Body household: Household,  @Header("Authorization") token: String): Call<Void>

    @PUT("api/household/{id}")
    fun updateHousehold(@Path("id") id: Long, @Body household: Household, @Header("Authorization") token: String): Call<Void>

    @POST("api/measurement")
    fun addNewMeasurement(@Body measurement: Measurement, @Header("Authorization") token: String): Call<Void>

    @GET("api/household/{householdId}/calculate/{utility}")
    fun getCalculation(@Path("householdId") householdId: Long, @Path("utility") utility: Int, @Header("Authorization") token: String): Call<Calculation?>

}
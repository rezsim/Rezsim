package com.project.rezsim.server.dto

import com.google.gson.annotations.SerializedName
import com.project.rezsim.server.dto.household.Household

data class User(

    val id: Long,

    val email: String,

    val password: String,

    val name: String,

    val status: Boolean,

    @SerializedName("registrationdate")
    val registrationDate: String,

    @SerializedName("lastsignin")
    val lastSignin: String,

    val households: Array<Household>

)

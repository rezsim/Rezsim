package com.project.rezsim.server.dto

data class User(

    val id: Long,

    val email: String,

    val password: String,

    val name: String,

    val status: Boolean,

    val registrationDate: String,

    val lastSignin: String,

    val households: List<Household>

)

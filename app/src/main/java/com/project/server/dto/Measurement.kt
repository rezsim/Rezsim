package com.project.server.dto

data class Measurement(

    val id: Long,

    val userId: Long,

    val householdId: Long,

    val utility: Int,

    val period: Int,

    val date: String,

    val position: Int,

    val consumption: Int,

    val level: Int

)

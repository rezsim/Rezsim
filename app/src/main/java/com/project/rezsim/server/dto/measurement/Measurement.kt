package com.project.rezsim.server.dto.measurement

data class Measurement(

    val id: Long,

    val userId: Long,

    var householdId: Long,

    val utility: Int,

    val period: Int,

    val date: String,

    val position: Int,

    val consumption: Int,

    val level: Int

)

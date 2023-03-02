package com.project.rezsim.server.dto.calculation

data class Calculation(

    val discountPrice: Int,

    val fullPrice: Int,

    val uplimit: Boolean,

    val underConsumption: Int,

    val overConsumption: Int

)

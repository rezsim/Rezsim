package com.project.server.dto

data class Household(

    val id: Long,

    val userId: Long,

    val name: String,

    val electricityUsage: Int,

    val electricityService: Int,

    val electricityPricingTypeA: Int,

    val electricityPricingTypeB: Int,

    val electricityPricingTypeH: Int,

    val electricityConsumptionUnit: Int,

    val gasService: Int,

    val gasChildren: Int,

    val gasConsumptionUnit: Int,

    val gasHeatingValue: Int,

    val measurements: List<Measurement>

)

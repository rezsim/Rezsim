package com.project.rezsim.server.dto

data class Household(

    val id: Long = -1,

    val userId: Long,

    var name: String = "",

    var electricityUsage: Int = -1,

    val electricityService: Int = -1,

    var electricityPricingTypeA: Int = -1,

    var electricityPricingTypeB: Int = -1,

    val electricityPricingTypeH: Int = -1,

    val electricityConsumptionUnit: Int = -1,

    val gasService: Int = -1,

    var gasChildren: Int = -1,

    val gasConsumptionUnit: Int = -1,

    var gasHeatingValue: Int = -1,

    val measurements: List<Measurement> = listOf()

)

package com.project.rezsim.server.dto.household

import com.project.rezsim.server.dto.measurement.Measurement
import com.project.rezsim.server.dto.measurement.Utility

data class Household(

    val id: Long = -1,

    val userId: Long,

    var name: String = "",

    var electricityUsage: Int = -1,

    val electricityService: Int = 0,

    var electricityPricingTypeA: Int = -1,

    var electricityPricingTypeB: Int = -1,

    val electricityPricingTypeH: Int = 0,

    val electricityConsumptionUnit: Int = 0,

    val gasService: Int = 0,

    var gasChildren: Int = -1,

    val gasConsumptionUnit: Int = 0,

    var gasHeatingValue: Int = -1,

    var electricityStatus: Int = 1,

    var gasStatus: Int = 1,

    var measurements: Array<Measurement> = arrayOf()

) {
    fun lastMeasurement(utility: Utility) = measurements
        .filter { it.utility == utility.value }
        .maxByOrNull { it.id }

}

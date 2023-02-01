package com.project.rezsim.ui.household

import androidx.annotation.IntegerRes
import androidx.lifecycle.MutableLiveData
import com.project.rezsim.base.RezsimViewModel
import com.project.server.dto.Household

class HouseholdViewModel : RezsimViewModel() {

    val contentLiveData = MutableLiveData<Household?>()
    val childrenValueLiveData = MutableLiveData<Int>()

    private var houseHold: Household? = null
    set(value) {
        field = value
        contentLiveData.postValue(value)
    }



    fun usageItems() = listOf("Kérem válasszon!", "Első elem", "Második elem", "Harmadik elem")

    fun pricingTypeAItems() = listOf("Kérem válasszon!", "Első elem", "Második elem", "Harmadik elem")

    fun pricingTypeBItems() = listOf("Kérem válasszon!", "Első elem", "Második elem", "Harmadik elem")

    fun childrenButtonClicked(oldValue: String?, add: Int) {
        val value = if (oldValue.isNullOrBlank()) 0 else oldValue.toInt()
        childrenValueLiveData.value = value + add
    }



}
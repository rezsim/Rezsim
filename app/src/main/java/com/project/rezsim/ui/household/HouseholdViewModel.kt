package com.project.rezsim.ui.household

import androidx.annotation.IntegerRes
import androidx.lifecycle.MutableLiveData
import com.project.rezsim.R
import com.project.rezsim.base.RezsimViewModel
import com.project.rezsim.ui.MainActivityViewModel
import com.project.server.UserModel
import com.project.server.dto.Household
import org.koin.core.component.inject

class HouseholdViewModel : RezsimViewModel() {

    val contentLiveData = MutableLiveData<Household?>()
    val childrenValueLiveData = MutableLiveData<Int>()

    private val userModel: UserModel by inject()
    private val mainActivityViewModel: MainActivityViewModel by inject()

    private var houseHold: Household? = null
    set(value) {
        field = value
        contentLiveData.postValue(value)
    }

    init {
        if (!userModel.hasHousehold()) {
            mainActivityViewModel.showMessage(R.string.household_electriciy_title)
        }
    }

    fun isCreatingNew() = houseHold == null

    fun usageItems() = listOf("Kérem válasszon!", "Első elem", "Második elem", "Harmadik elem")

    fun pricingTypeAItems() = listOf("Kérem válasszon!", "Első elem", "Második elem", "Harmadik elem")

    fun pricingTypeBItems() = listOf("Kérem válasszon!", "Első elem", "Második elem", "Harmadik elem")

    fun childrenButtonClicked(oldValue: String?, add: Int) {
        val value = if (oldValue.isNullOrBlank()) -1 else oldValue.toInt()
        childrenValueLiveData.value = value + add
    }



}
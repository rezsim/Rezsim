package com.project.rezsim.ui.screen.household

import androidx.lifecycle.MutableLiveData
import com.project.rezsim.R
import com.project.rezsim.base.RezsimViewModel
import com.project.rezsim.ui.screen.activity.MainActivityViewModel
import com.project.rezsim.ui.view.message.MessageSeverity
import com.project.rezsim.ui.view.message.MessageType
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
            mainActivityViewModel.showMessage(messageResId = R.string.household_no_household_yet, type = MessageType.SNACKBAR_MANUALCLOSE)
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

    fun save(data: Household) {
        if (data.electricityUsage == -1
            || data.electricityPricingTypeA == -1
            || data.electricityPricingTypeB == -1
            || data.electricityUsage == -1
            || data.gasChildren == -1
            || data.gasHeatingValue == -1
            || data.name.isEmpty()
        ) {
            mainActivityViewModel.showMessage(messageResId = R.string.fill_all_mandatory, severity = MessageSeverity.ERROR)
        } else {

        }
    }




}
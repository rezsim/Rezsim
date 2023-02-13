package com.project.rezsim.ui.screen.household

import androidx.lifecycle.MutableLiveData
import com.project.rezsim.R
import com.project.rezsim.base.RezsimViewModel
import com.project.rezsim.device.StringRepository
import com.project.rezsim.ui.screen.activity.MainActivityViewModel
import com.project.rezsim.ui.view.message.MessageSeverity
import com.project.rezsim.ui.view.message.MessageType
import com.project.rezsim.server.UserModel
import com.project.rezsim.server.dto.Household
import com.project.rezsim.ui.screen.main.MainFragment
import org.koin.core.component.inject

class HouseholdViewModel : RezsimViewModel() {

    val contentLiveData = MutableLiveData<Content?>()
    val childrenValueLiveData = MutableLiveData<Int>()
    val hasElectricityLiveData = MutableLiveData<Boolean>()
    val hasGasLiveData = MutableLiveData<Boolean>()

    private val userModel: UserModel by inject()
    private val mainActivityViewModel: MainActivityViewModel by inject()
    private val stringRepository: StringRepository by inject()

    var houseHold: Household? = null
    set(value) {
        field = value
        contentLiveData.postValue(value?.let { Content.fromHousehold(value) } )
    }

    init {
        if (!userModel.hasHousehold()) {
            mainActivityViewModel.showMessage(messageResId = R.string.household_no_household_yet, type = MessageType.SNACKBAR_MANUALCLOSE)
            houseHold = null
        }
    }

    fun isCreatingNew() = houseHold == null

    fun usageItems() = items("household_usage_mode")

    fun pricingTypeAItems() = items("household_pricing_mode_a")

    fun pricingTypeBItems() = items("household_pricing_mode_b")

    fun heatingValueItems() = items("household_gas_heating")

    fun childrenButtonClicked(oldValue: String?, add: Int) {
        val value = if (oldValue.isNullOrBlank()) -1 else oldValue.toInt()
        childrenValueLiveData.value = value + add
    }

    fun electricitySwitchChanged(selected: Boolean) {
        hasElectricityLiveData.value = selected
    }

    fun gasSwitchChanged(selected: Boolean) {
        hasGasLiveData.value = selected
    }

    fun save(data: Content) {
        var complet = true
        if (hasElectricityLiveData.value == true) {
            if (isCreatingNew() && data.electricityMeter == -1) {
                complet = false
            }
            if (data.electricityUseMode == -1 || data.electricityPricingA == -1 || data.electricityPricingB != -1) {
                complet = false
            }
        }
        if (hasGasLiveData.value == true) {
            if (isCreatingNew() && data.gasMeter == -1) {
                complet = false
            }
            if (data.gasHeating == -1 || data.gasChildren == -1) {
                complet = false
            }
        }
        if (data.name.isEmpty() || !complet) {
            mainActivityViewModel.showMessage(messageResId = R.string.fill_all_mandatory, severity = MessageSeverity.ERROR)
        } else {
            mainActivityViewModel.showMessage(message = "Háztartás mentése még kidolgozás alatt.", severity = MessageSeverity.ERROR)

/*
            val h = houseHold ?: Household(
                userId = userModel.getUser()?.id ?: error ("No user when save household."),
            )
            data.applyOnHousehold(h)
*/
        }
    }

    private fun items(baseRresourceName: String): List<String> = mutableListOf(stringRepository.getById(R.string.household_item_select)).apply {
        for (i in 0..100) {
            val name = "${baseRresourceName}_$i"
            if (stringRepository.isExistsByName(name)) {
                add(stringRepository.getByNameForceNotNull(name))
            } else {
                return@apply
            }
        }
    }.toList()


}
package com.project.rezsim.ui.screen.household

import androidx.lifecycle.MutableLiveData
import com.project.rezsim.R
import com.project.rezsim.base.RezsimViewModel
import com.project.rezsim.device.StringRepository
import com.project.rezsim.ui.screen.activity.MainActivityViewModel
import com.project.rezsim.ui.view.message.MessageSeverity
import com.project.rezsim.ui.view.message.MessageType
import com.project.rezsim.server.UserModel
import com.project.rezsim.server.dto.household.*
import com.project.rezsim.server.household.HouseholdRepository
import org.koin.core.component.inject

class HouseholdViewModel : RezsimViewModel() {

    val contentLiveData = MutableLiveData<Content?>()
    val childrenValueLiveData = MutableLiveData<Int>()
    val hasElectricityLiveData = MutableLiveData<Boolean>()
    val hasGasLiveData = MutableLiveData<Boolean>()

    private val userModel: UserModel by inject()
    private val mainActivityViewModel: MainActivityViewModel by inject()
    private val stringRepository: StringRepository by inject()
    private val householdRepository: HouseholdRepository by inject()

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

    fun usageItems() = ElectricityUsage.values().toList().sortedBy { it.value }.map { stringRepository.getById(it.textResId) }.toList()

    fun pricingTypeAItems() = ElectricityPricingTypeA.values().toList().sortedBy { it.value }.map { stringRepository.getById(it.textResId) }.toList()

    fun pricingTypeBItems() = ElectricityPricingTypeB.values().toList().sortedBy { it.value }.map { stringRepository.getById(it.textResId) }.toList()

    fun heatingValueItems() = HeatingValue.values().toList().sortedBy { it.value }.map { stringRepository.getById(it.textResId) }.toList()

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
            if (data.electricityUseMode == -1 || data.electricityPricingA == -1 || data.electricityPricingB == -1) {
                complet = false
            }
        }
        if (hasGasLiveData.value == true) {
            if (data.gasHeating == -1 || data.gasChildren == -1) {
                complet = false
            }
        }
        if (data.name.isEmpty() || !complet) {
            mainActivityViewModel.showMessage(messageResId = R.string.fill_all_mandatory, severity = MessageSeverity.ERROR)
        } else {
            if (isCreatingNew()) {
                val h = Household(
                    userId = userModel.getUser()?.id ?: error ("No user when save household."),
                )
                data.applyOnHousehold(h)
                mainActivityViewModel.showProgress()
                householdRepository.addNewHousehold(h, userModel.getToken()!!).observeForever {
                    mainActivityViewModel.hideProgress()
                    if (!it) {
                        mainActivityViewModel.showMessage("Nem sikerült!", MessageType.SNACKBAR_CLOSEABLE_AND_MANUALCLOSE, MessageSeverity.ERROR)
                    }
                }
            } else {

            }
        }
    }


}
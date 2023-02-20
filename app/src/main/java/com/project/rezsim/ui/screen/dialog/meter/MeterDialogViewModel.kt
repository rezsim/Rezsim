package com.project.rezsim.ui.screen.dialog.meter

import android.os.Bundle
import com.project.rezsim.R
import com.project.rezsim.base.RezsimViewModel
import com.project.rezsim.device.StringRepository
import com.project.rezsim.server.UserModel
import com.project.rezsim.server.dto.measurement.Utility
import com.project.rezsim.ui.screen.dialog.message.MessageDialogFragment
import com.project.rezsim.ui.view.message.MessageType
import org.koin.core.component.inject
import java.util.*

class MeterDialogViewModel : RezsimViewModel() {

    lateinit var date: Calendar
    lateinit var utility: Utility
    var householdId: Long = -1

    private val stringRepository: StringRepository by inject()
    private val userModel: UserModel by inject()

    fun parseArguments(arg: Bundle?) {
        arg?.let {
            utility = Utility.valueOf(it.getString(MeterDialogFragment.ARG_UTILITY, ""))
            householdId = it.getLong(MeterDialogFragment.ARG_HOUSEHOLD)
            date = Calendar.getInstance(Locale.getDefault())
        }
    }

    fun message() = stringRepository.getById(if (utility == Utility.GAS) R.string.dialog_meter_read_utility_gas else R.string.dialog_meter_read_utility_electricity).let {
        stringRepository.getById(R.string.dialog_meter_read_message, it)
    }

    fun iconRes() = if (utility == Utility.GAS) R.drawable.ic_gas else R.drawable.ic_electricity

    fun lastValue() = userModel.getUser()
        ?.households?.find { it.id == householdId }
        ?.measurements?.findLast { it.utility == utility.value }
        ?.position?.toString()

    fun save(position: Int) {

    }


}
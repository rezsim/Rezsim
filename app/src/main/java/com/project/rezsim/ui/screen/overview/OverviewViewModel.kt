package com.project.rezsim.ui.screen.overview

import com.project.rezsim.R
import com.project.rezsim.base.RezsimViewModel
import com.project.rezsim.device.StringRepository
import com.project.rezsim.server.UserModel
import com.project.rezsim.server.dto.household.Household
import com.project.rezsim.server.dto.measurement.Utility
import org.koin.core.component.inject

class OverviewViewModel : RezsimViewModel() {

    private val userModel: UserModel by inject()

    lateinit var household: Household
    lateinit var utility: Utility

    private val stringRepository: StringRepository by inject()

    fun title() = stringRepository.getById(if (utility == Utility.GAS) R.string.overview_header_title_gas else R.string.overview_header_title_electricity)

//    fun months() =



}
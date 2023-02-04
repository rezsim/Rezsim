package com.project.rezsim.ui.screen.main

import android.content.Context
import android.util.Log
import com.project.rezsim.R
import com.project.rezsim.base.RezsimViewModel
import com.project.rezsim.server.UserModel
import org.koin.core.component.inject

class MainViewModel : RezsimViewModel() {

    private val userModel: UserModel by inject()

    fun householdItems(): List<String> = (userModel.getUser()?.households?.map { it.name }?.toMutableList() ?: mutableListOf()).apply {
        add(0, context.resources.getString(R.string.main_spinner_household_prompt))
    }.toList()

    fun householdSelected(householdIndex: Int) {
        Log.d("DEBINFO-R", "householdIndex:$householdIndex")
    }

}
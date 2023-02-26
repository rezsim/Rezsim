package com.project.rezsim.device

import android.content.Context
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class StringRepository : KoinComponent {

    private val context : Context by inject()

    fun getByNameForceNotNull(name: String) = getByName(name) ?: ""

    fun getByName(name: String) = getIdByName(name).let {
        if (it == 0)
            null
        else
            getById(it)
    }

    fun getById(id: Int, param: Any? = null, param1: Any? = null) = if (param == null) {
        context.resources.getString(id)
    } else if (param1 == null) {
        context.resources.getString(id, param)
    } else {
        context.resources.getString(id, param, param1)
    }

    fun isExistsByName(name: String) = getIdByName(name) != 0

    private fun getIdByName(name: String) = context.resources.getIdentifier(name, "string", context.packageName)

    private fun getNameById(id: Int) = context.resources.getResourceEntryName(id)

}
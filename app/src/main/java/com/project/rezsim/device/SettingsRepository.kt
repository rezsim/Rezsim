package com.project.rezsim.device

import android.content.Context
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SettingsRepository : KoinComponent {

    private val context: Context by inject()

    private val settings = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun readUserEmail() = settings.getString(KEY_USER_EMAIL, null)
    fun writeUserEmail(email: String) = settings.edit().apply {
        putString(KEY_USER_EMAIL, email)
        apply()
    }

    fun readUserPassword() = settings.getString(KEY_USER_PASSWORD, null)
    fun writeUserPassword(password: String) = settings.edit().apply {
        putString(KEY_USER_PASSWORD, password)
        apply()
    }

    fun readLastHouseholdId() = settings.getLong(KEY_LAST_HOUSEHOLD_ID, 0)
    fun writeLastHouseholdId(lastHouseholdId: Long) = settings.edit().apply {
        putLong(KEY_LAST_HOUSEHOLD_ID, lastHouseholdId)
        apply()
    }

    fun readOverviewMonthSelectorVisible() = settings.getBoolean(KEY_OVERVIEW_MONTH_SELECTOR_VISIBLE, false)
    fun writeOverviewMonthSelectorVisible(visible: Boolean) = settings.edit().apply {
        putBoolean(KEY_OVERVIEW_MONTH_SELECTOR_VISIBLE, visible)
        apply()
    }

    fun clearUser() {
        settings.edit().apply {
            remove(KEY_USER_EMAIL)
            remove(KEY_USER_PASSWORD)
            apply()
        }
    }

    fun clearPassword() {
        settings.edit().remove(KEY_USER_PASSWORD).apply()
    }

    fun clear() {
        settings.edit().apply {
            clear()
            apply()
        }
    }

    companion object {
        private const val SHARED_PREFERENCE_NAME = "RezsimApp"

        private const val KEY_USER_EMAIL = "UserEmail"
        private const val KEY_USER_PASSWORD = "UserPassword"
        private const val KEY_LAST_HOUSEHOLD_ID = "LastHouseholdId"
        private const val KEY_OVERVIEW_MONTH_SELECTOR_VISIBLE = "OverviewMonthSelectorVisible"

    }
}
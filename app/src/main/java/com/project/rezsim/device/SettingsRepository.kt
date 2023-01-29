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

    fun clearUser() {
        settings.edit().apply {
            remove(KEY_USER_EMAIL)
            remove(KEY_USER_PASSWORD)
            apply()
        }
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

    }
}
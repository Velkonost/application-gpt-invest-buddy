package com.investbuddy.common.data

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Префы для данных приложения.
 * НЕ очищается при разлогине.
 */
@Singleton
class AppSharedPreferences @Inject constructor(@ApplicationContext val context: Context) {

    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun getBoolean(key: SharedPrefsKeys): Boolean {
        return prefs.getBoolean(key.name, true)
    }

    fun getBoolean(key: SharedPrefsKeys, defValue: Boolean): Boolean {
        return prefs.getBoolean(key.name, defValue)
    }

    fun setBoolean(key: SharedPrefsKeys, value: Boolean) {
        prefs.edit {
            putBoolean(key.name, value)
        }
    }

    fun setString(key: SharedPrefsKeys, value: String) {
        prefs.edit {
            putString(key.name, value)
        }
    }

    fun getString(key: SharedPrefsKeys): String? {
        return prefs.getString(key.name, "")
    }

    fun clearAll() {
        prefs.edit() {
            clear()
        }
    }
}

package com.investbuddy.common.data

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Префы для данных пользователя.
 * Очищается при разлогине.
 */
@Singleton
class UserSharedPreferences @Inject constructor(@ApplicationContext val context: Context) {

    private val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun getBoolean(key: SharedPrefsKeys): Boolean {
        return prefs.getBoolean(key.name, false)
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

    @Deprecated("добавлено для возможности сохранения модифицированного ключа, не использовать")
    fun setLong(key: String, value: Long) {
        prefs.edit {
            putLong(key, value)
        }
    }

    @Deprecated("добавлено для возможности сохранения модифицированного ключа, не использовать")
    fun getLong(key: String): Long {
        return prefs.getLong(key, 0L)
    }

    @Deprecated("добавлено для возможности сохранения модифицированного ключа, не использовать")
    fun removeKey(key: String) {
        prefs.edit {
            remove(key)
        }
    }
}

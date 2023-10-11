package com.investbuddy.common.util

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Класс-мост между активити и глубинами сетевого слоя.
 * Нужен для логаута пользователя при 401 ошибке.
 */
@Singleton
class LogoutHandlerProvider @Inject constructor() {

    private lateinit var currentLogoutHandler: LogoutHandler

    fun onLogout() {
        currentLogoutHandler.handleLogout()
    }

    fun setCurrentLogoutHandler(logoutHandler: LogoutHandler) {
        currentLogoutHandler = logoutHandler
    }
}

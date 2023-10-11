package com.investbuddy.core.network.model

data class ServerError(
    val message: String,
    val code: Int
) {

    companion object {
        val DEFAULT = ServerError("Unknown error", 0)
        val MAINTENANCE = ServerError("Maintenance", 503)
    }
}

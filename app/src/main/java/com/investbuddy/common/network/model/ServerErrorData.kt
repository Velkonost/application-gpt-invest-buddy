package com.investbuddy.common.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ServerErrorData(
    val message: String,
    val debug: String? = null
) {

    companion object {
        val DEFAULT = ServerErrorData("Unknown error", "")
    }
}

package com.investbuddy.core.extension

fun Long.convertMillisecondsToMmSs(): String {
    val seconds = this / 1000
    val s = seconds % 60
    val m = seconds / 60 % 60
    return String.format("%02d:%02d", m, s)
}

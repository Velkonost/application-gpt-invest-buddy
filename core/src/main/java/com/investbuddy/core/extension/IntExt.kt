package com.investbuddy.core.extension

fun Int.convertSecondsToMmSs(): String {
    val seconds = this
    val s = seconds % 60
    val m = seconds / 60 % 60
    return String.format("%02d:%02d", m, s)
}
package com.investbuddy.core.extension

import androidx.fragment.app.Fragment


inline fun <reified T> Fragment.requireArgument(key: String): Lazy<T> =
    lazy {
        val rawArgument = requireArguments().get(key) ?: throw IllegalArgumentException(
            "The Fragment $this does not have an argument with the key \"$key\""
        )
        val argument = rawArgument as? T ?: throw ClassCastException(
            "Can't cast an argument with the key \"$key\" to ${T::class.java}"
        )
        argument
    }
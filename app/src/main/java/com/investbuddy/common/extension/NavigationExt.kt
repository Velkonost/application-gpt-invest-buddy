package com.investbuddy.common.extension

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

fun Fragment.navigateTo(
    @IdRes destinationId: Int,
    bundle: Bundle? = null
) {
    findNavController().navigate(destinationId, bundle)
}

fun Fragment.navigateBack() {
    val popped = findNavController().popBackStack()
    if (!popped) {
        requireActivity().onBackPressed()
    }
}

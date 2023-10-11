package com.investbuddy.core.extension

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetBehavior

fun Int.toPx() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Int.toDp() = (this / Resources.getSystem().displayMetrics.density).toInt()

@Suppress("DEPRECATION")
fun Activity.getDisplayMetrics(): DisplayMetrics {
    return DisplayMetrics().also {
        windowManager
            .defaultDisplay
            .getMetrics(it)
    }
}

fun Context.showNotImplementedMsg() {
    Toast.makeText(this, "Не реализовано", Toast.LENGTH_LONG).show()
}

fun Context.browse(url: String): Boolean =
    try {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
        true
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
        false
    }

fun BottomSheetBehavior<out ViewGroup>.changeBottomSheetState() {
    if (state == BottomSheetBehavior.STATE_EXPANDED) {
        state = BottomSheetBehavior.STATE_COLLAPSED
    } else if (state == BottomSheetBehavior.STATE_COLLAPSED) {
        state = BottomSheetBehavior.STATE_EXPANDED
    }
}

fun Context.share(text: String, title: String = ""): Boolean =
    try {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, text)
        intent.putExtra(Intent.EXTRA_TITLE, title)

        startActivity(Intent.createChooser(intent, title))
        true
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
        false
    }

fun SpannableString.setClickableSpan(string: String, listener: () -> Unit) {
    val firstIndex = indexOf(string = string, ignoreCase = true)
    if (firstIndex == -1) return
    val clickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            listener.invoke()
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.linkColor = ds.color
            ds.isUnderlineText = true
        }
    }
    setSpan(
        clickableSpan,
        firstIndex,
        firstIndex + string.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )
}


fun Context.dpToPx(valueInDp: Float) : Float{
    val displayMetrics = resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, displayMetrics)
}
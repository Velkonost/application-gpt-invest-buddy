package com.investbuddy.common.ui

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.widget.NestedScrollView

/**
 * NestedScrollView с возмжностью заблочить скролл
 * **/
class LockableNestedScrollView : NestedScrollView {
    var scrollable = true

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return scrollable && super.onTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return scrollable && super.onInterceptTouchEvent(ev)
    }

}
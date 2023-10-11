package com.investbuddy.common.ui

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior

/**
 * BottomSheetBehaviour, где drag BottomSheet происходит только при взаимодействии с anchorView
 * **/
class AnchorViewDraggableBottomSheetBehaviour<V : View> : BottomSheetBehavior<V> {
    var anchorView: View? = null

    constructor() : super()

    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)

    override fun onInterceptTouchEvent(
        parent: CoordinatorLayout,
        child: V,
        event: MotionEvent
    ): Boolean {
        anchorView?.let {
            isDraggable = parent.isPointInChildBounds(it, event.x.toInt(), event.y.toInt())
        }
        return super.onInterceptTouchEvent(parent, child, event)
    }
}
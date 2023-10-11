package com.investbuddy.common.ui

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.investbuddy.R

open class RoundedCornersBottomSheetDialogFragment : BottomSheetDialogFragment() {

    override fun getTheme(): Int {
        return R.style.RoundedCornersBottomSheetDialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        view.setBackgroundResource(R.drawable.bg_rounded_corners_bottomsheet)
        (requireDialog() as BottomSheetDialog).behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }
}
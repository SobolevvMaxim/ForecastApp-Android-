package com.example.features

import android.graphics.Color
import android.util.DisplayMetrics
import android.view.Gravity
import androidx.fragment.app.DialogFragment
import kotlin.math.roundToInt

object DialogFragmentSetup {
    fun DialogFragment.alignToLeft() {
        dialog?.window?.apply {
            setGravity(Gravity.START)
            decorView.apply {

                // Get screen width
                val displayMetrics = DisplayMetrics().apply {
                    windowManager.defaultDisplay.getMetrics(this)
                }

                setBackgroundColor(Color.WHITE) // I don't know why it is required, without it background of rootView is ignored (is transparent even if set in xml/runtime)
                minimumHeight = displayMetrics.heightPixels
                minimumWidth = (displayMetrics.widthPixels * 0.75).roundToInt()
                setPadding(0, 0, 0, 0)
                invalidate()
            }
        }
    }
}
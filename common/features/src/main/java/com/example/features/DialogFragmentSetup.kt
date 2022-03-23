package com.example.features

import android.graphics.Color
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import kotlin.math.roundToInt

object DialogFragmentSetup {
    fun DialogFragment.alignToLeft() {
        dialog?.window?.apply {
            setGravity(Gravity.START)
            decorView.apply {
                val getMetrics: () -> (Pair<Int, Int>) = {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                        val metrics = context.getSystemService(
                            WindowManager::class.java
                        ).currentWindowMetrics
                        Pair(metrics.bounds.height(), metrics.bounds.width())
                    } else {
                        val displayMetrics = DisplayMetrics().apply {
                            @Suppress("DEPRECATION")
                            windowManager.defaultDisplay.getMetrics(this)
                        }
                        Pair(displayMetrics.heightPixels, displayMetrics.widthPixels)
                    }
                }

                val metricsValues = getMetrics()

                setBackgroundColor(Color.WHITE) // I don't know why it is required, without it background of rootView is ignored (is transparent even if set in xml/runtime)
                minimumHeight = metricsValues.first
                minimumWidth = (metricsValues.second * 0.75).roundToInt()
                setPadding(0, 0, 0, 0)
                invalidate()
            }
        }
    }
}
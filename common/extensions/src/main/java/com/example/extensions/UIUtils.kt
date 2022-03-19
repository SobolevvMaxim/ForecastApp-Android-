package com.example.extensions

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import kotlin.math.roundToInt

object UIUtils {

    fun TextView.networkCheckByUI(): Boolean = !this.isVisible

    fun ProgressBar.updateProgressBar(visible: Boolean) {
        when (visible) {
            true -> this.visibility = View.VISIBLE
            false -> this.visibility = View.INVISIBLE
        }
    }

    fun Double.getTemperature() = "${this.roundToInt() - 273}°"
}
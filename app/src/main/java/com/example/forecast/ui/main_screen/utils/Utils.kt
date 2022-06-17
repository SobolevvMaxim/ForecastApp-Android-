package com.example.forecast.ui.main_screen.utils

import android.widget.Toast
import androidx.core.text.trimmedLength
import androidx.fragment.app.Fragment
import com.example.extensions.UIUtils.networkCheckByUI
import com.example.forecast.R
import kotlinx.android.synthetic.main.main_page_fragment.*

object Utils {
    fun String.getForecastImageID(): Int {
        return when (this) {
            "Rain" -> R.drawable.forecast_rain_icon
            "Snow" -> R.drawable.forecast_snow_icon
            "Clear" -> R.drawable.forecast_sun_icon
            else -> R.drawable.forecast_clouds_icon
        }
    }

    fun Fragment.checkCityInput(cityInput: String): Boolean {
        return when (cityInput.trimmedLength()) {
            in 0..3 -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.incorrect_input),
                    Toast.LENGTH_LONG
                ).show()
                false
            }
            else -> {
                return when (offline_mode.networkCheckByUI()) {
                    true -> {
                        true
                    }
                    false -> {
                        Toast.makeText(
                            context,
                            getString(R.string.network_unavailable),
                            Toast.LENGTH_SHORT
                        ).show()
                        false
                    }
                }
            }
        }
    }
}
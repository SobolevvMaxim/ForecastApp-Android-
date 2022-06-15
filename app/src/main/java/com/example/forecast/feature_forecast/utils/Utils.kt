package com.example.forecast.feature_forecast.utils

import com.example.forecast.R

object Utils {
    fun String.getForecastImageID(): Int {
        return when (this) {
            "Rain" -> R.drawable.forecast_rain_icon
            "Snow" -> R.drawable.forecast_snow_icon
            "Clear" -> R.drawable.forecast_sun_icon
            else -> R.drawable.forecast_clouds_icon
        }
    }
}
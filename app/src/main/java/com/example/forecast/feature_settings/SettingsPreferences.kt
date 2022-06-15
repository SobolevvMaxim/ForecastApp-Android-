package com.example.forecast.feature_settings

import android.content.Context
import android.content.SharedPreferences
import com.example.forecast.R
import com.example.forecast.domain.data_processing.AutoUpdateTime
import com.example.forecast.domain.data_processing.TemperatureUnit

class SettingsPreferences(
    private val preferences: SharedPreferences,
    private val context: Context,
) {
    fun getTemperatureUnit(): TemperatureUnit =
        TemperatureUnit.fromString(
            preferences.getString(
                context.getString(
                    R.string.key_temperature_unit
                ), ""
            )!!
        )

    fun getAutoUpdateTime(): AutoUpdateTime =
        AutoUpdateTime.fromString(preferences.getString(
            context.getString(R.string.key_auto_update), ""
        )!!)
}
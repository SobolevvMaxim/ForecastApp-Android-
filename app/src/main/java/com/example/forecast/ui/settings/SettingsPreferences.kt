package com.example.forecast.ui.settings

import android.content.Context
import androidx.preference.PreferenceManager
import com.example.forecast.R
import com.example.forecast.domain.data_processing.AutoUpdateTime
import com.example.forecast.domain.data_processing.TemperatureUnit
import com.example.forecast.domain.prefstore.ISettingsPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SettingsPreferences @Inject constructor(
    @ApplicationContext private val context: Context,
) : ISettingsPreferences {
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    override fun getTemperatureUnit(): TemperatureUnit =
        TemperatureUnit.fromString(
            preferences?.getString(
                context.getString(
                    R.string.key_temperature_unit
                ), ""
            ) ?: ""
        )

    override fun getAutoUpdateTime(): AutoUpdateTime =
        AutoUpdateTime.fromString(
            preferences?.getString(
                context.getString(R.string.key_auto_update), ""
            ) ?: ""
        )
}
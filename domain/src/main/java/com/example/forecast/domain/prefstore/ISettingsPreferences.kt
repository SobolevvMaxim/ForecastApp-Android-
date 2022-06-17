package com.example.forecast.domain.prefstore

import com.example.forecast.domain.data_processing.AutoUpdateTime
import com.example.forecast.domain.data_processing.TemperatureUnit

interface ISettingsPreferences {
    fun getTemperatureUnit(): TemperatureUnit

    fun getAutoUpdateTime(): AutoUpdateTime
}
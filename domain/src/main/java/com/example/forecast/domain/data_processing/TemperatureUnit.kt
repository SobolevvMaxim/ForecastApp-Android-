package com.example.forecast.domain.data_processing

import java.text.DecimalFormat
import java.util.*

private val NUMBER_FORMAT = DecimalFormat("#")

enum class TemperatureUnit {
    FAHRENHEIT,
    CELSIUS,
    KELVIN;

    override fun toString() = when (this) {
        FAHRENHEIT -> "imperial"
        CELSIUS -> "metric"
        KELVIN -> "kelvin"
    }

    fun format(temperatureInKelvin: Double): String {
        return NUMBER_FORMAT.format(
            UnitConverter.convertTemperature(
                temperatureInKelvin,
                this
            )
        ) + symbol()
    }

    private fun symbol(): String {
//        return when (this) {
//            FAHRENHEIT -> "°F"
//            CELSIUS -> "°C"
//            KELVIN -> "k"
//        }
        return "°"
    }

    companion object {
        fun fromString(s: String): TemperatureUnit {
            return when (s.lowercase(Locale.ROOT)) {
                "celsius" -> CELSIUS
                "metric" -> CELSIUS
                "kelvin" -> KELVIN
                "fahrenheit" -> FAHRENHEIT
                "imperial" -> FAHRENHEIT
                else -> CELSIUS
            }
        }
    }
}
package com.example.forecast.feature_settings

import java.text.DecimalFormat

private val NUMBER_FORMAT = DecimalFormat("#.#")

enum class TemperatureUnit {
    FAHRENHEIT,
    CELSIUS,
    KELVIN;

    override fun toString() = when (this) {
        FAHRENHEIT -> "imperial"
        CELSIUS -> "metric"
        KELVIN -> ""
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
        return when (this) {
            FAHRENHEIT -> "°F"
            CELSIUS -> "°C"
            KELVIN -> "K"
        }
    }

    companion object {
        fun fromString(s: String): TemperatureUnit {
            return when (s.toLowerCase()) {
                "celsius" -> CELSIUS
                "metric" -> CELSIUS
                "kelvin" -> KELVIN
                "" -> KELVIN
                "fahrenheit" -> FAHRENHEIT
                "imperial" -> FAHRENHEIT
                else -> throw IllegalStateException()
            }
        }
    }
}
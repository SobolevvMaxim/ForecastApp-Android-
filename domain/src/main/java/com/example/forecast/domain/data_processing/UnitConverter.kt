package com.example.forecast.domain.data_processing

object UnitConverter {
    /**
     * Convert [temperatureInKelvin] from [KELVIN] to [to]
     */
    fun convertTemperature(temperatureInKelvin: Double, to: TemperatureUnit): Double {
        return when (to) {
            TemperatureUnit.KELVIN -> temperatureInKelvin
            TemperatureUnit.CELSIUS -> temperatureInKelvin - 272.15
            TemperatureUnit.FAHRENHEIT -> temperatureInKelvin * 9.0 / 5 - 459.67
        }
    }
}
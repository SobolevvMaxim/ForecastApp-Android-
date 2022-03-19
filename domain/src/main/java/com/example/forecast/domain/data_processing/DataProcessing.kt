package com.example.forecast.domain.data_processing

import com.example.forecast.domain.model.CityWeather
import kotlin.math.roundToInt

class DataProcessing(val city: CityWeather) {

    fun getForecastLocation() = "${city.name}, ${city.country}"

    fun getTemperature() = "${city.dailyTemperatures[0].temp.roundToInt() - 273}°"

    fun getUVI() = city.uvi.toString()

    fun getFeelsLike(stringTitle: String) = "$stringTitle ${city.feels_like.roundToInt() - 273}°"

    fun getForecastDate() = city.forecastDate

    fun getHumidity() = "${city.humidity}%"
}
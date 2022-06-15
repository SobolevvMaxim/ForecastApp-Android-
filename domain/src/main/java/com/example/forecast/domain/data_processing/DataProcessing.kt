package com.example.forecast.domain.data_processing

import com.example.forecast.domain.data_processing.Extensions.getCelsius
import com.example.forecast.domain.data_processing.Extensions.getTemperatureByUnit
import com.example.forecast.domain.model.CityWeather

class DataProcessing(val city: CityWeather, private val unit: TemperatureUnit) {

    fun getForecastLocation() = "${city.name}, ${city.country}"

    fun getTemperature() = city.dailyTemperatures[0].temp.getTemperatureByUnit(unit)

    fun getUVI() = city.uvi.toString()

    fun getFeelsLike(stringTitle: String) = "$stringTitle ${city.feels_like.getCelsius()}"

    fun getForecastDate() = city.forecastDate

    fun getHumidity() = "${city.humidity}%"
}
package com.example.forecast.domain.data_processing

import com.example.forecast.domain.data_processing.Extensions.getCelsius
import com.example.forecast.domain.model.CityWeather

class DataProcessing(val city: CityWeather) {

    fun getForecastLocation() = "${city.name}, ${city.country}"

    fun getTemperature() = city.dailyTemperatures[0].temp.getCelsius()

    fun getUVI() = city.uvi.toString()

    fun getFeelsLike(stringTitle: String) = "$stringTitle ${city.feels_like.getCelsius()}"

    fun getForecastDate() = city.forecastDate

    fun getHumidity() = "${city.humidity}%"
}
package com.example.extensions

import com.example.forecast.domain.model.CityWeather
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    fun SimpleDateFormat.getCityForecastDate(city: CityWeather) =
        this.parse(city.forecastDate) ?: Date(1)

    fun SimpleDateFormat.getTime(time: String): String = this.format(Date(time.toLong()))
}
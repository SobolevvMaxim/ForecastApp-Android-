package com.example.extensions

import com.example.forecast.domain.model.CityWeather
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    fun SimpleDateFormat.getCityForecastDate(city: CityWeather) =
        this.parse(city.forecastDate) ?: Date(1)

    fun SimpleDateFormat.getTime(time: String): String = this.format(Date(time.toLong() * 1000))

    fun CityWeather.checkIfDeprecated(
        dateFormat: SimpleDateFormat,
        deprecatedAction: (deprecatedCityForecast: CityWeather) -> Unit
    ) {
        val cityDate = Calendar.getInstance()
        val currentDate = Calendar.getInstance()

        cityDate.apply {
            time = dateFormat.getCityForecastDate(this@checkIfDeprecated)
            add(Calendar.HOUR, 1)
        }

        if (cityDate.time.before(currentDate.time)) {
            deprecatedAction(this)
        }
    }
}
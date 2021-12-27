package com.example.homeworksandroid.responces

import com.example.homeworksandroid.CityWeather
import com.example.homeworksandroid.DailyForecast
import com.google.gson.annotations.SerializedName
import kotlin.math.roundToInt

data class TemperatureResponse(
    @SerializedName("daily") val daily: List<Daily>,
) {
    fun getTemperature(city: CityWeather) = city.apply {
        val result = ArrayList(daily.map {
            DailyForecast(temp = it.temp.day.roundToInt() - 273, description = it.weather[0].main)
        })
        temperatures = result
    }
}

data class Daily(
    val temp: Temp,
    val weather: List<WeatherData>
)

data class Temp(
    val day: Double,
)

data class WeatherData(
    val id: String?,
    val main: String,
    val description: String,
)
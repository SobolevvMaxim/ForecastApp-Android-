package com.example.forecast.responces

import com.example.forecast.CityWeather
import com.example.forecast.Daily
import com.google.gson.annotations.SerializedName
import kotlin.math.roundToInt

data class TemperatureResponse(
    @SerializedName("daily") val dailyTemp: List<DailyTemp>,
) {
    fun getTemperature(city: CityWeather) = city.apply {
        val result = ArrayList(dailyTemp.map {
            Daily(temp = it.temp.day.roundToInt() - 273, description = it.weather[0].main)
        })
        temperatures = result
    }
}

data class DailyTemp(
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
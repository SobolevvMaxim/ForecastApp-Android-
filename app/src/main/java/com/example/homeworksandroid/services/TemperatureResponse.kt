package com.example.homeworksandroid.services

import android.util.Log
import com.example.homeworksandroid.CityWeather
import com.google.gson.annotations.SerializedName
import kotlin.math.roundToInt

data class TemperatureResponse(
    val lat: String,
    val lon: String,
    val timezone: String,
    val timezoneOffset: String,
    val current: Any,
    val minutely: Any,
    val hourly: Any,
    @SerializedName("daily")val daily: List<Daily>,
    val alerts: Any
) {
    fun getTemperature(city: CityWeather) = city.apply {
        Log.d("MY_ERROR", "changing for the city $city, daily $daily")
        val result = ArrayList<Pair<Int, String>> (emptyList())
        for(i in daily.indices)
            result.add(Pair(daily[i].temp.day.roundToInt() - 273, daily[i].weather[0].main))
        Log.d("MY_ERROR", "city chacnged $this")
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
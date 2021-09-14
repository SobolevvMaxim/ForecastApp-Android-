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
    val current: Current,
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

data class Current(
    val dt: String?,
    val sunrise: String?,
    val sunset: String?,
    val temp: String?,
    val f: String?,
    val press: String?,
    val hum: String?,
    val dew: String?,
    val uvi: String?,
    val clouds: String?,
    val vis: String?,
    val wind: String?,
    val windDeg: String?,
    val weather: Any?,
    val minutely: Any?,
    val hourly: Any?,
    val daily: List<Daily>
)

data class Daily(
    val dt: String?,
    val sunrise: String?,
    val sunset: String?,
    val moon: String?,
    val moonSet: String?,
    val phase: String?,
    val temp: Temp,
    val fells: Any?,
    val press: String?,
    val hum: String?,
    val dew: String?,
    val w1: String?,
    val w2: String?,
    val w3: String?,
    val clouds: String?,
    val uvi: String?,
    val pop: String?,
    val rain: String?,
    val snow: String?,
    val weather: List<WeatherData>
)

data class Temp(
    val day: Double,
    val min: String?,
    val max: String?,
    val night: String?,
    val eve: String?,
    val morn: String?
)

data class WeatherData(
    val id: String?,
    val main: String,
    val description: String,
    val icon: Any?
)
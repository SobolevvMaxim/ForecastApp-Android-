package com.example.homeworksandroid.responces

import android.util.Log
import com.example.homeworksandroid.CityWeather
import com.google.gson.annotations.SerializedName
import kotlin.math.roundToInt

data class TemperatureResponse(
    @SerializedName("daily") val daily: List<Daily>,
) {
    fun getTemperature(city: CityWeather) = city.apply {
        Log.d("MY_ERROR", "changing for the city $city, daily $daily")
        val result = ArrayList<Pair<Int, String>>(emptyList())
        for (i in daily.indices)
            result.add(Pair(daily[i].temp.day.roundToInt() - 273, daily[i].weather[0].main))
        Log.d("MY_ERROR", "city changed $this")
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
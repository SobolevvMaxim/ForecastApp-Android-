package com.example.api.dto

import com.example.forecast.domain.model.City
import com.example.forecast.domain.model.CityWeather
import com.example.forecast.domain.model.Daily
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

data class TemperaturesDto(
    @SerializedName("daily") val dailyTemp: List<DailyTemp>,
) {
    fun toCityWeather(city: City, chosen: Boolean = false): CityWeather {
        val result = ArrayList(dailyTemp.map {
            Daily(temp = it.temp.day.roundToInt() - 273, description = it.weather[0].main)
        })
        return CityWeather(
            id = city.id,
            name = city.name,
            country = city.country,
            lat = city.coordinates.lat,
            lon = city.coordinates.lon,
            temperatures = result,
            forecastDate = SimpleDateFormat("dd.MM.yyyy",
                Locale.getDefault()).format(Calendar.getInstance().time),
            chosen = chosen
        )
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
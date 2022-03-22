package com.example.remote.dto

import com.google.gson.annotations.SerializedName

data class TemperaturesDto(
    @SerializedName("hourly") val hourlyTemp: List<HourlyTemp>,
    @SerializedName("daily") val dailyTemp: List<DailyTemp>,
    @SerializedName("current") val current: Current,
)

data class Current(
    val sunrise: String,
    val sunset: String,
    val feels_like: Double,
    val humidity: Int,
    val uvi: Double
)

data class DailyTemp(
    val temp: Temp,
    val weather: List<WeatherData>
)

data class HourlyTemp(
    val temp: Double,
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
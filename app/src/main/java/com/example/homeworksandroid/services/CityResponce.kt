package com.example.homeworksandroid.services

import com.example.homeworksandroid.CityWeather
import com.google.gson.annotations.SerializedName
import kotlin.math.roundToInt

data class CityResponse(
    val coord: Any,
    @SerializedName("weather") val weather: List<Weather>,
    val base: String,
    @SerializedName("main") val main: Temperature,
    val visibility: Int,
    val wind: Any,
    val clouds: Any,
    val dt: Any,
    @SerializedName("sys") val sys: Sys,
    val timezone: Any,
    @SerializedName("id") val id: String,
    val name: String,
    val cod: Int


) {
    fun toCityWeather(name: String) = CityWeather(
        name = name,
        id = id,
        country = sys.country,
        temp = main.temp.roundToInt() - 273,
        description = weather[0].main
    )
}

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Temperature(
    val temp: Double,
    val feelsLike: Double,
    val tempMin: Double,
    val tempMax: Double,
    val pressure: Int,
    val humidity: Int
)

data class Sys(
    val type: Int,
    val ID: Int,
    val message: Double,
    val country: String
)
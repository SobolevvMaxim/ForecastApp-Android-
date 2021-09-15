package com.example.homeworksandroid.services

import com.example.homeworksandroid.CityWeather
import com.google.gson.annotations.SerializedName
import kotlin.math.roundToInt

data class CoordinatesResponse(
    @SerializedName("coord")val coord: Coord,
    val weather: Any,
    val base: String,
    val main: Any,
    val visibility: Int,
    val wind: Any,
    val clouds: Any,
    val dt: Any,
    @SerializedName("sys") val sys: Sys,
    val timezone: Any,
    val id: String,
    val name: String,
    val cod: Int


) {
    fun toCityWeather(name: String) = CityWeather(
        name = name,
        id = id,
        country = sys.country,
        lat = coord.lat,
        lon = coord.lon,
    )
}

data class Sys(
    val type: Int,
    val ID: Int,
    val message: Double,
    val country: String
)

data class Coord(
    val lat: String,
    val lon: String
)
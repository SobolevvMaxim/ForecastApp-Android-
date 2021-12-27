package com.example.homeworksandroid.responces

import com.example.homeworksandroid.CityWeather
import com.example.homeworksandroid.DailyForecast
import com.example.homeworksandroid.FORMAT
import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList


data class CoordinatesResponse(
    @SerializedName("coord") val coord: Coord,
    @SerializedName("sys") val sys: Sys,
    val id: String,
) {
    fun toCityWeather(name: String) = CityWeather(
        name = name,
        id = id,
        country = sys.country,
        lat = coord.lat,
        lon = coord.lon,
        forecastDate = FORMAT.format(Calendar.getInstance().time),
        temperatures = ArrayList()
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
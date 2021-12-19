package com.example.homeworksandroid.responces

import android.annotation.SuppressLint
import com.example.homeworksandroid.CityWeather
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("ConstantLocale")
val FORMAT = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

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
        forecastDate = FORMAT.format(Calendar.getInstance().time)
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
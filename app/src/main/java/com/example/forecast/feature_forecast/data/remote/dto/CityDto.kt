package com.example.forecast.feature_forecast.data.remote.dto

import com.example.forecast.feature_forecast.data.local.entities.CityWeatherEntity
import com.example.forecast.feature_forecast.domain.model.CityWeather
import com.example.forecast.feature_forecast.domain.model.Daily
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

data class CityDto(
    @SerializedName("coord") val coord: Coord,
    @SerializedName("sys") val sys: Sys,
    val id: String,
) {
    fun toCityWeatherEntity(name: String) = CityWeatherEntity(
        name = name,
        id = id,
        country = sys.country,
        lat = coord.lat,
        lon = coord.lon,
        forecastDate = SimpleDateFormat("dd.MM.yyyy",
            Locale.getDefault()).format(Calendar.getInstance().time),
        temperatures = ArrayList()
    )
}

data class Sys(
    val type: Int,
    val ID: Int,
    val message: Double,
    val country: String,
)

data class Coord(
    val lat: String,
    val lon: String,
)
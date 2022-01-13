package com.example.forecast.feature_forecast.data.remote.dto

import com.example.forecast.feature_forecast.domain.model.City
import com.google.gson.annotations.SerializedName

data class CityDto(
    @SerializedName("coord") val coord: Coord,
    @SerializedName("sys") val sys: Sys,
    val id: String,
) {
    fun toCity(name: String) = City(
        coord = coord,
        id = id,
        name = name,
        country = sys.country
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
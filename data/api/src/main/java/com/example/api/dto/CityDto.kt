package com.example.api.dto

import com.example.forecast.domain.model.City
import com.example.forecast.domain.model.Coordinates
import com.example.forecast.domain.model.Sys
import com.google.gson.annotations.SerializedName

data class CityDto(
    @SerializedName("coord") val coordinates: Coordinates,
    @SerializedName("sys") val sys: Sys,
    val id: String,
) {
    fun toCity(name: String) = City(
        coordinates = coordinates,
        id = id,
        name = name,
        country = sys.country
    )
}
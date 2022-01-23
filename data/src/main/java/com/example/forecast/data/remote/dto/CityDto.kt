package com.example.forecast.data.remote.dto

import com.example.forecast.domain.model.City
import com.example.forecast.domain.model.Coord
import com.example.forecast.domain.model.Sys
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
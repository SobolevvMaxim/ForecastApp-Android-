package com.example.remote.dto

import com.example.forecast.domain.model.Coordinates
import com.example.forecast.domain.model.Sys
import com.google.gson.annotations.SerializedName

data class CityDto(
    @SerializedName("coord") val coordinates: Coordinates,
    @SerializedName("sys") val sys: Sys,
    val id: String,
) {

}
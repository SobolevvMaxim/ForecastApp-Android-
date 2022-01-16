package com.example.forecast.feature_forecast.domain.model

import com.example.forecast.feature_forecast.data.remote.dto.Coord
import com.example.forecast.feature_forecast.data.remote.dto.Sys

data class City(
    val coord: Coord,
    val id: String,
    val name: String,
    val country: String,
)
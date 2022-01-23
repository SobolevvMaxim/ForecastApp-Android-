package com.example.forecast.domain.model

data class City(
    val coord: Coord,
    val id: String,
    val name: String,
    val country: String,
)
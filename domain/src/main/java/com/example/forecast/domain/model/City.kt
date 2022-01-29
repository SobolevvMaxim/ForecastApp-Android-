package com.example.forecast.domain.model

data class City(
    val coordinates: Coordinates,
    val id: String,
    val name: String,
    val country: String,
)
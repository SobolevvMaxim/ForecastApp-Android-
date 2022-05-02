package com.example.forecast.domain.model

data class CityToSearch(
    val coordinates: Coordinates? = null,
    val searchName: String,
)
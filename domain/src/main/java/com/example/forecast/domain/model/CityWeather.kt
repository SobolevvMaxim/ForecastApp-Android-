package com.example.forecast.domain.model

data class CityWeather(
    val id: String,
    val name: String,
    val country: String,
    var lat: String = "",
    var lon: String = "",
    var dailyTemperatures: ArrayList<Daily>,
    var hourlyTemperatures: ArrayList<Hourly>,
    val sunrise: String,
    val sunset: String,
    val feels_like: Double,
    val humidity: Int,
    val uvi: Double,
    var forecastDate: String,
) {

    fun toCity() = City(
        coordinates = Coordinates(
            lat = lat,
            lon = lon
        ),
        id = id,
        name = name,
        country = country
    )
}
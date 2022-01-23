package com.example.forecast.domain.model

data class CityWeather(
    val id: String,
    val name: String,
    val country: String,
    var lat: String = "",
    var lon: String = "",
    var temperatures: ArrayList<Daily>,
    var chosen: Boolean = false,
    val forecastDate: String,
) {

    fun toCity() = City(
        coord = Coord(
            lat = lat,
            lon = lon
        ),
        id = id,
        name = name,
        country = country
    )
}
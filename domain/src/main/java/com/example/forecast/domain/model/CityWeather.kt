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

    override fun equals(other: Any?): Boolean {
        if (other is CityWeather) {
            return other.id == this.id
        }
        return (other == this)
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }

    override fun toString(): String {
        return "id: $id name: $name date: $forecastDate"
    }
}
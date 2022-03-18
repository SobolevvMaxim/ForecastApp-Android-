package com.example.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.forecast.domain.model.CityWeather
import com.example.forecast.domain.model.Daily
import com.example.forecast.domain.model.Hourly
import com.example.local.DailyTemperatureConverter
import com.example.local.HourlyTemperatureConverter

@Entity(tableName = com.example.local.TABLE_NAME)
data class CityWeatherEntity(
    @PrimaryKey val id: String,
    val name: String,
    val country: String,
    var lat: String = "",
    var lon: String = "",
    @TypeConverters(DailyTemperatureConverter::class) var dailyTemperatures: ArrayList<Daily>,
    @TypeConverters(HourlyTemperatureConverter::class) var hourlyTemperatures: ArrayList<Hourly>,
    val sunrise: String,
    val sunset: String,
    val feels_like: Double,
    val humidity: Int,
    val uvi: Double,
    val forecastDate: String,
) {
    fun toCityWeather(): CityWeather {
        return CityWeather(
            id,
            name,
            country,
            lat,
            lon,
            dailyTemperatures,
            hourlyTemperatures,
            sunrise,
            sunset,
            feels_like,
            humidity,
            uvi,
            forecastDate
        )
    }
}

fun CityWeather.toCityWeatherEntity() = CityWeatherEntity(
    id,
    name,
    country,
    lat,
    lon,
    dailyTemperatures,
    hourlyTemperatures,
    sunrise,
    sunset,
    feels_like,
    humidity,
    uvi,
    forecastDate
)
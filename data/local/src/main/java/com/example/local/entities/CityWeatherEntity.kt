package com.example.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.forecast.domain.model.Daily
import com.example.forecast.domain.model.Hourly
import com.example.local.TABLE_NAME
import com.example.local.converters.DailyTemperatureConverter
import com.example.local.converters.HourlyTemperatureConverter

@Entity(tableName = TABLE_NAME)
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
)
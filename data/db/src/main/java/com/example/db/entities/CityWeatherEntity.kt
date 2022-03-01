package com.example.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.db.DailyTemperatureConverter
import com.example.db.HourlyTemperatureConverter
import com.example.forecast.domain.model.CityWeather
import com.example.forecast.domain.model.Daily
import com.example.forecast.domain.model.Hourly

@Entity(tableName = com.example.db.TABLE_NAME)
data class CityWeatherEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "country") val country: String,
    var lat: String = "",
    var lon: String = "",
    @TypeConverters(DailyTemperatureConverter::class) var dailyTemperatures: ArrayList<Daily>,
    @TypeConverters(HourlyTemperatureConverter::class) var hourlyTemperatures: ArrayList<Hourly>,
    @ColumnInfo(name = "forecastDate") val forecastDate: String,
) {
    fun toCityWeather(): CityWeather {
        return CityWeather(
            id = id,
            name = name,
            country = country,
            lat = lat,
            lon = lon,
            dailyTemperatures = dailyTemperatures,
            hourlyTemperatures = hourlyTemperatures,
            forecastDate = forecastDate
        )
    }
}

fun CityWeather.toCityWeatherEntity() = CityWeatherEntity(
    id, name, country, lat, lon, dailyTemperatures, hourlyTemperatures,  forecastDate
)
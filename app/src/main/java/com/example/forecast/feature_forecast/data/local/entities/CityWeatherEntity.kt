package com.example.forecast.feature_forecast.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.forecast.feature_forecast.data.local.TABLE_NAME
import com.example.forecast.feature_forecast.data.local.TemperatureConverter
import com.example.forecast.feature_forecast.domain.model.CityWeather
import com.example.forecast.feature_forecast.domain.model.Daily

@Entity(tableName = TABLE_NAME)
data class CityWeatherEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "country") val country: String,
    var lat: String = "",
    var lon: String = "",
    @TypeConverters(TemperatureConverter::class) var temperatures: ArrayList<Daily>,
    var chosen: Boolean = false,
    @ColumnInfo(name = "forecastDate") val forecastDate: String,
) {
    fun toCityWeather(): CityWeather {
        return CityWeather(
            id = id,
            name = name,
            country = country,
            lat = lat,
            lon = lon,
            temperatures = temperatures,
            chosen = chosen,
            forecastDate = forecastDate
        )
    }
}

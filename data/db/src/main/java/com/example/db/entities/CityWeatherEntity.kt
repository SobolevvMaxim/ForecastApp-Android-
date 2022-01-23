package com.example.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.db.TemperatureConverter
import com.example.forecast.domain.model.CityWeather
import com.example.forecast.domain.model.Daily

@Entity(tableName = com.example.db.TABLE_NAME)
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

fun CityWeather.toCityWeatherEntity() = CityWeatherEntity(
    id, name, country, lat, lon, temperatures, chosen, forecastDate
)
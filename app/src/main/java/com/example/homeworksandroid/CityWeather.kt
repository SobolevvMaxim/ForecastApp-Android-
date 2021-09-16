package com.example.homeworksandroid

import androidx.room.*
import com.example.homeworksandroid.database.TemperatureConverter

const val TABLE_NAME = "CITIES_TABLE"

@Entity(tableName = TABLE_NAME)
data class CityWeather(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "country") val country: String,
    var lat: String = "",
    var lon: String = "",
    @TypeConverters(TemperatureConverter::class) var temperatures: ArrayList<Pair<Int, String>> = mutableListOf<Pair<Int, String>>() as ArrayList<Pair<Int, String>>,
    var chosen: Boolean = false
)
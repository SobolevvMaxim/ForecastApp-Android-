package com.example.homeworksandroid

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.*
import com.example.homeworksandroid.database.TemperatureConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

const val TABLE_NAME = "CITIES_TABLE"

@Entity(tableName = TABLE_NAME)
data class CityWeather @RequiresApi(Build.VERSION_CODES.O) constructor(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "country") val country: String,
    var lat: String = "",
    var lon: String = "",
    @TypeConverters(TemperatureConverter::class) var temperatures: ArrayList<Pair<Int, String>> = mutableListOf<Pair<Int, String>>() as ArrayList<Pair<Int, String>>,
    var chosen: Boolean = false,
    @ColumnInfo(name = "forecastDate") val forecastDate: String
)
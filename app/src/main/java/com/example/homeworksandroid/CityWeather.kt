package com.example.homeworksandroid

import android.annotation.SuppressLint
import androidx.room.*
import com.example.homeworksandroid.database.TemperatureConverter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

const val TABLE_NAME = "CITIES_TABLE"
@SuppressLint("ConstantLocale")
val FORMAT = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

@Entity(tableName = TABLE_NAME)
data class CityWeather constructor(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "country") val country: String,
    var lat: String = "",
    var lon: String = "",
    @TypeConverters(TemperatureConverter::class) var temperatures: ArrayList<Pair<Int, String>> = mutableListOf<Pair<Int, String>>() as ArrayList<Pair<Int, String>>,
    var chosen: Boolean = false,
    @ColumnInfo(name = "forecastDate") val forecastDate: String
)
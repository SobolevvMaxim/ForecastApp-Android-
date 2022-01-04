package com.example.forecast.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.forecast.CityWeather

@Database(entities = [CityWeather::class], version = 1)
@TypeConverters(TemperatureConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun citiesDao(): CitiesDao
}
package com.example.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.local.entities.CityWeatherEntity

@Database(entities = [CityWeatherEntity::class], version = 1)
@TypeConverters(DailyTemperatureConverter::class, HourlyTemperatureConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun citiesDao(): CityWeatherDao
}
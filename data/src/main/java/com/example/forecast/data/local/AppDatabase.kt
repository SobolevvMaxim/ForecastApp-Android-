package com.example.forecast.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.forecast.data.local.entities.CityWeatherEntity

@Database(entities = [CityWeatherEntity::class], version = 1)
@TypeConverters(TemperatureConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun citiesDao(): CitiesDao
}
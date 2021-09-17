package com.example.homeworksandroid.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.homeworksandroid.CityWeather

@Database(entities = [CityWeather::class], version = 1)
@TypeConverters(TemperatureConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun citiesDao(): CitiesDao
}
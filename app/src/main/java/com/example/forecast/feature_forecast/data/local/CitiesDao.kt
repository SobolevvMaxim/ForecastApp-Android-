package com.example.forecast.feature_forecast.data.local

import androidx.room.*
import com.example.forecast.feature_forecast.data.local.entities.CityWeatherEntity

const val TABLE_NAME = "CITIES_TABLE"

@Dao
interface CitiesDao {

    @Query("SELECT * FROM $TABLE_NAME")
    fun getAll(): List<CityWeatherEntity>

    @Query("SELECT * FROM $TABLE_NAME WHERE name = :name")
    fun getCityForecastByName(name: String): CityWeatherEntity

    @Query("SELECT * FROM $TABLE_NAME WHERE chosen = 1")
    fun getChosenCity(): CityWeatherEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(city: CityWeatherEntity)

    @Update
    fun update(city: CityWeatherEntity)

    @Delete
    fun delete(city: CityWeatherEntity)
}
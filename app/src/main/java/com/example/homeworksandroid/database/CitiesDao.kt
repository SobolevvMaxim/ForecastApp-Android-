package com.example.homeworksandroid.database

import androidx.room.*
import com.example.homeworksandroid.CityWeather

const val TABLE_NAME = "CITIES_TABLE"

@Dao
interface CitiesDao {

    @Query("SELECT * FROM $TABLE_NAME")
    fun getAll(): List<CityWeather>

    @Query("SELECT * FROM $TABLE_NAME WHERE name = :name")
    fun getCityForecastByName(name: String): CityWeather

    @Query("SELECT * FROM $TABLE_NAME WHERE chosen = 1")
    fun getChosenCity(): CityWeather

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(city: CityWeather)

    @Update
    fun update(city: CityWeather)

    @Delete
    fun delete(city: CityWeather)
}
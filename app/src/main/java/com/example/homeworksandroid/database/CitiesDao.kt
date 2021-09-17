package com.example.homeworksandroid.database

import androidx.room.*
import com.example.homeworksandroid.CityWeather
import com.example.homeworksandroid.TABLE_NAME

@Dao
interface CitiesDao {

    @Query("SELECT * FROM $TABLE_NAME")
    fun getAll(): List<CityWeather>

    @Query("SELECT * FROM $TABLE_NAME WHERE name = :name")
    fun getCityForecastByName(name: String): CityWeather

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(city: CityWeather)

    @Update
    fun update(city: CityWeather)

    @Delete
    fun delete(city: CityWeather)
}
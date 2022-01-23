package com.example.forecast.data.local

import androidx.room.*
import com.example.forecast.data.local.entities.CityWeatherEntity

const val TABLE_NAME = "CITIES_TABLE"

@Dao
interface CityWeatherDao {

    /**
     * @return all forecasts from base
     */
    @Query("SELECT * FROM $TABLE_NAME")
    fun getAll(): List<CityWeatherEntity>

    /**
     * @param name city name to get forecast
     * @return city forecast by name
     */
    @Query("SELECT * FROM $TABLE_NAME WHERE name = :name")
    fun getCityForecastByName(name: String): CityWeatherEntity

    /**
     * @return current chosen city from base
     */
    @Query("SELECT * FROM $TABLE_NAME WHERE chosen = 1")
    fun getChosenCity(): CityWeatherEntity?

    /**
     * insert city in base (OnConflict = REPLACE)
     * @param city city to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(city: CityWeatherEntity)

    /**
     * update city in base
     * @param city city to update
     */
    @Update
    fun update(city: CityWeatherEntity)

    /**
     * delete city in base by id
     * @param cityID id of city to delete in base
     */
    @Query("DELETE FROM $TABLE_NAME WHERE id = :cityID")
    fun delete(cityID: String)
}
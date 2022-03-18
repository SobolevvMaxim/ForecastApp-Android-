package com.example.local

import androidx.room.*
import com.example.local.entities.CityWeatherEntity

const val TABLE_NAME = "CITIES_TABLE"

@Dao
interface CityWeatherDao {

    /**
     * Get cities from base
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

    /**
     * get city from base by id
     * @param cityID id of the city to get
     * @return city weather entity from base
     */
    @Query("SELECT * FROM $TABLE_NAME WHERE id = :cityID")
    fun getCityByID(cityID: String): CityWeatherEntity?
}
package com.example.forecast.domain.repository

import com.example.forecast.domain.model.CityToSearch
import com.example.forecast.domain.model.CityWeather

interface IForecastRepository {
    /**
     * Search city by name to get needed info
     * @param query city name to search
     * @return City()
     */
    suspend fun searchCity(query: String): Result<CityToSearch>

    /**
     * Search forecast by City
     * @param cityToSearch cearch forecast for current city
     * @return City forecast with all info
     */
    suspend fun searchForecastByCoordinates(cityToSearch: CityToSearch): Result<CityWeather>

    /**
     * Write city forecast to base
     * @param city city forecast to write
     */
    suspend fun writeCityToBase(city: CityWeather)

    /**
     * Update deprecated city forecast
     * @param city city to update
     * @return all cities from base
     */
    suspend fun updateCityInBase(city: CityWeather)

    /**
     * Delete city from base and cities list
     * @param city city to delete
     * @return all cities from base
     */
    suspend fun deleteCityInBase(city: CityWeather): Set<CityWeather>

    /**
     * Get all cities from base
    =     * @return all cities from base
     */
    suspend fun getAll(): Set<CityWeather>

    /**
     * Get city forecast from base by id
     * @param cityID city id to get from base
     * @return city forecast with current id
     */
    suspend fun getCityByID(cityID: String): CityWeather?
}
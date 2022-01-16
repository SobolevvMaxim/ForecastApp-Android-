package com.example.forecast.feature_forecast.domain.repository

import com.example.forecast.feature_forecast.domain.model.City
import com.example.forecast.feature_forecast.domain.model.CityWeather

interface IForecastRepository {
    suspend fun searchForecast(city: City): Result<CityWeather>

    suspend fun writeCityToBase(city: CityWeather): Set<CityWeather>

    suspend fun getAll(): Set<CityWeather>

    suspend fun changeChosenCityByName(lastChosenIndex: Int, newChosenIndex: Int)

    suspend fun searchCity(query: String): Result<City>

    suspend fun updateCityInBase(city: CityWeather): Set<CityWeather>

    suspend fun getChosenCityFromBase(): CityWeather?

    suspend fun deleteCityInBase(city: CityWeather): Set<CityWeather>
}
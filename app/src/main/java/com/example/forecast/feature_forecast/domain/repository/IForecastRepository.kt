package com.example.forecast.feature_forecast.domain.repository

import com.example.forecast.feature_forecast.domain.model.City
import com.example.forecast.feature_forecast.domain.model.CityWeather

interface IForecastRepository {
    suspend fun searchTemp(city: City): Result<CityWeather>

    suspend fun writeCityToBase(city: CityWeather): Set<CityWeather>

    suspend fun getAll(): Set<CityWeather>

    suspend fun changeChosenCityByName(lastChosenIndex: Int, newChosenIndex: Int)

    suspend fun search(query: String): Result<City>
}
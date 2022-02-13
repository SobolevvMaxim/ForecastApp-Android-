package com.example.forecast.domain.repository

import com.example.forecast.domain.model.City
import com.example.forecast.domain.model.CityWeather

interface IForecastRepository {
    suspend fun searchCity(query: String): Result<City>

    suspend fun searchForecast(city: City): Result<CityWeather>

    suspend fun writeCityToBase(city: CityWeather): Set<CityWeather>

    suspend fun updateCityInBase(city: CityWeather): Set<CityWeather>

    suspend fun deleteCityInBase(city: CityWeather): Set<CityWeather>

    suspend fun getAll(): Set<CityWeather>
}
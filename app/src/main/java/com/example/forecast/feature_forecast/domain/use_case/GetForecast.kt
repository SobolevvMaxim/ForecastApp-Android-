package com.example.forecast.feature_forecast.domain.use_case

import com.example.forecast.feature_forecast.data.repository.ForecastRepository
import com.example.forecast.feature_forecast.domain.model.City
import com.example.forecast.feature_forecast.domain.model.CityWeather

class GetForecast(
    val repository: ForecastRepository,
) {

    suspend operator fun invoke(city: City): Result<CityWeather> {
        return repository.searchForecast(city)
    }
}
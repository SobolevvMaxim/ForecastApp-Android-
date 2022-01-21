package com.example.forecast.feature_forecast.domain.use_case

import com.example.forecast.feature_forecast.data.repository.ForecastRepository
import com.example.forecast.feature_forecast.domain.model.CityWeather
import com.example.forecast.feature_forecast.domain.repository.IForecastRepository

class DeleteCity(
    private val repository: IForecastRepository
) {

    suspend operator fun invoke(city: CityWeather): Set<CityWeather> =
        repository.deleteCityInBase(city = city)
}
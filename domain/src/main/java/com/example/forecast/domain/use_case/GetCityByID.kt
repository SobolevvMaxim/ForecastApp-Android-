package com.example.forecast.domain.use_case

import com.example.forecast.domain.model.CityWeather
import com.example.forecast.domain.repository.IForecastRepository

class GetCityByID(
    private val repository: IForecastRepository,
) {

    suspend operator fun invoke(cityID: String): CityWeather? =
        repository.getCityByID(cityID)
}
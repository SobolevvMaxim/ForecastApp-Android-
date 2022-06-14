package com.example.forecast.domain.use_case

import com.example.forecast.domain.model.CityToSearch
import com.example.forecast.domain.repository.IForecastRepository

class GetCityInfo(
    private val repository: IForecastRepository,
) {

    suspend operator fun invoke(cityName: String): Result<CityToSearch> {
        return repository.searchCity(cityName)
    }
}
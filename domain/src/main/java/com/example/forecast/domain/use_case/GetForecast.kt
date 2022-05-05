package com.example.forecast.domain.use_case

import com.example.forecast.domain.model.CityToSearch
import com.example.forecast.domain.model.CityWeather
import com.example.forecast.domain.repository.IForecastRepository

class GetForecast(
    val repository: IForecastRepository,
) {

    suspend operator fun invoke(cityToSearch: CityToSearch): Result<CityWeather> {
        return repository.searchForecastByCoordinates(cityToSearch)
    }
}
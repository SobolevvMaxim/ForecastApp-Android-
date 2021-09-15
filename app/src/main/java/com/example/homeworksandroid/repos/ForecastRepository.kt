package com.example.homeworksandroid.repos

import com.example.homeworksandroid.CityWeather
import com.example.homeworksandroid.services.TemperatureService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ForecastRepository(
    private val temperatureService: TemperatureService
) {
    suspend fun searchTemp(city: CityWeather): Result<CityWeather> {
        return withContext(Dispatchers.IO){
            kotlin.runCatching {
                temperatureService.searchTempAsync(lat = city.lat, lon = city.lon)
                    .await()
                    .takeIf { it.isSuccessful }
                    ?.body()
                    ?.getTemperature(city)
                    ?: throw Exception("empty data")
            }
        }
    }
}
package com.example.homeworksandroid.repos

import com.example.homeworksandroid.CityWeather
import com.example.homeworksandroid.services.CitiesService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class CitiesRepository(
    private val citiesService: CitiesService
) {
    suspend fun search(query: String): Result<CityWeather> {
        return withContext(Dispatchers.IO) {
            kotlin.runCatching {
                citiesService.searchCityAsync(query = query)
                    .await()
                    .takeIf { it.isSuccessful }
                    ?.body()
                    ?.toCityWeather(query)
                    ?: throw Exception("Empty data")
            }
        }
    }
}
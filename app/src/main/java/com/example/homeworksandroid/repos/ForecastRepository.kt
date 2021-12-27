package com.example.homeworksandroid.repos

import com.example.homeworksandroid.CityWeather
import com.example.homeworksandroid.database.CitiesDao
import com.example.homeworksandroid.services.TemperatureService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ForecastRepository(
    private val temperatureService: TemperatureService,
    private val citiesDao: CitiesDao
) {
    private var addedCities: Set<CityWeather>? = null

    suspend fun searchTemp(city: CityWeather): Result<CityWeather> {
        return withContext(Dispatchers.IO) {
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

    suspend fun writeCityToBase(city: CityWeather): Set<CityWeather> {
        withContext(Dispatchers.IO) {
            citiesDao.insert(city = city)
            insertInMemory(city = city)
        }

        return addedCities ?: emptySet()
    }

    private fun insertInMemory(city: CityWeather) {
        addedCities = addedCities?.toMutableSet()?.let { cities ->
            cities.add(city)
            cities
        } ?: setOf(city)
    }

    suspend fun getAll(): Set<CityWeather> {
        if (addedCities == null) {
            addedCities = withContext(Dispatchers.IO) {
                citiesDao.getAll().toSet()
            }
        }

        return addedCities ?: emptySet()
    }

    suspend fun changeChosenCityByName(lastChosenIndex: Int, newChosenIndex: Int) {
        withContext(Dispatchers.IO) {
            addedCities = addedCities?.toMutableSet()?.let { cities ->
                cities.elementAt(lastChosenIndex).chosen = false
                cities.elementAt(newChosenIndex).chosen = true
                cities
            }

            changeChosenCityInBase(lastChosenIndex, newChosenIndex)
        }
    }

    private suspend fun changeChosenCityInBase(lastChosenIndex: Int, newChosenIndex: Int) {
        withContext(Dispatchers.IO) {
            addedCities?.let {
                val lastChosen = it.elementAt(lastChosenIndex)
                val newChosen = it.elementAt(newChosenIndex)

                citiesDao.apply {
                    update(lastChosen)
                    update(newChosen)
                }
            }
        }
    }

    fun isDbEmpty(): Boolean = addedCities.isNullOrEmpty()
}
package com.example.forecast.repos

import com.example.forecast.CityWeather
import com.example.forecast.database.CitiesDao
import com.example.forecast.services.CitiesService
import com.example.forecast.services.TemperatureService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface IForecastRepository {
    suspend fun searchTemp(city: CityWeather): Result<CityWeather>

    suspend fun writeCityToBase(city: CityWeather): Set<CityWeather>

    suspend fun getAll(): Set<CityWeather>

    suspend fun changeChosenCityByName(lastChosenIndex: Int, newChosenIndex: Int)

    suspend fun search(query: String): Result<CityWeather>
}

class ForecastRepository @Inject constructor(
    private val temperatureService: TemperatureService,
    private val citiesService: CitiesService,
    private val citiesDao: CitiesDao
): IForecastRepository {
    private var addedCities: Set<CityWeather>? = null

    override suspend fun search(query: String): Result<CityWeather> {
        return withContext(Dispatchers.IO) {
            kotlin.runCatching {
                citiesService.searchCityAsync(query = query)
                    .await()
                    .takeIf { it.isSuccessful }
                    ?.body()
                    ?.toCityWeather(query)
                    ?: throw java.lang.Exception("Empty data")
            }
        }
    }

    override suspend fun searchTemp(city: CityWeather): Result<CityWeather> {
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

    override suspend fun writeCityToBase(city: CityWeather): Set<CityWeather> {
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

    override suspend fun getAll(): Set<CityWeather> {
        if (addedCities == null) {
            addedCities = withContext(Dispatchers.IO) {
                citiesDao.getAll().toSet()
            }
        }

        return addedCities ?: emptySet()
    }

    override suspend fun changeChosenCityByName(lastChosenIndex: Int, newChosenIndex: Int) {
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
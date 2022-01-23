package com.example.forecast.data.repository

import com.example.forecast.data.local.CityWeatherDao
import com.example.forecast.data.local.entities.toCityWeatherEntity
import com.example.forecast.data.remote.services.CitiesService
import com.example.forecast.data.remote.services.TemperatureService
import com.example.forecast.domain.model.City
import com.example.forecast.domain.model.CityWeather
import com.example.forecast.domain.repository.IForecastRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ForecastRepository @Inject constructor(
    private val temperatureService: TemperatureService,
    private val citiesService: CitiesService,
    private val cityWeatherDao: CityWeatherDao,
) : IForecastRepository {
    private var addedCities: Set<CityWeather>? = null

    override suspend fun searchCity(query: String): Result<City> {
        return withContext(Dispatchers.IO) {
            kotlin.runCatching {
                citiesService.searchCityAsync(query = query)
                    .await()
                    .takeIf { it.isSuccessful }
                    ?.body()
                    ?.toCity(name = query)
                    ?: throw java.lang.Exception("Empty data")
            }
        }
    }

    override suspend fun searchForecast(city: City): Result<CityWeather> {
        return withContext(Dispatchers.IO) {
            kotlin.runCatching {
                temperatureService.searchTempAsync(lat = city.coord.lat, lon = city.coord.lon)
                    .await()
                    .takeIf { it.isSuccessful }
                    ?.body()
                    ?.toCityWeather(city)
                    ?: throw Exception("empty data")
            }
        }
    }

    override suspend fun writeCityToBase(city: CityWeather): Set<CityWeather> {
        withContext(Dispatchers.IO) {
            cityWeatherDao.insert(city = city.toCityWeatherEntity())
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

    override suspend fun updateCityInBase(city: CityWeather): Set<CityWeather> {
        withContext(Dispatchers.IO) {
            cityWeatherDao.update(city.toCityWeatherEntity())
            updateInMemory(city)
        }

        return addedCities ?: emptySet()
    }

    private fun updateInMemory(city: CityWeather) {
        addedCities = addedCities?.toMutableSet()?.let { cities ->
            val temp = cities.filter { it.id != city.id }.toMutableSet()
            temp.add(city)
            temp.toSet()
        } ?: setOf(city)
    }

    override suspend fun getAll(): Set<CityWeather> {
        if (addedCities == null) {
            addedCities = withContext(Dispatchers.IO) {
                cityWeatherDao.getAll().map { it.toCityWeather() }.toSet()
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

                cityWeatherDao.apply {
                    update(lastChosen.toCityWeatherEntity())
                    update(newChosen.toCityWeatherEntity())
                }
            }
        }
    }

    override suspend fun getChosenCityFromBase(): CityWeather? {
        return withContext(Dispatchers.IO) {
            cityWeatherDao.getChosenCity()?.toCityWeather()
        }
    }

    override suspend fun deleteCityInBase(city: CityWeather): Set<CityWeather> {
        withContext(Dispatchers.IO) {
            cityWeatherDao.delete(cityID = city.id)
            deleteFromMemory(city = city)
        }

        return addedCities ?: emptySet()
    }

    private fun deleteFromMemory(city: CityWeather) {
        addedCities = addedCities?.toMutableSet()?.let { cities ->
            cities.remove(city)
            cities
        }
    }
}
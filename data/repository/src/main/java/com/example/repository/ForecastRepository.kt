package com.example.repository

import com.example.api.services.CitiesService
import com.example.api.services.TemperatureService
import com.example.db.CityWeatherDao
import com.example.db.entities.toCityWeatherEntity
import com.example.forecast.domain.model.City
import com.example.forecast.domain.model.CityWeather
import com.example.forecast.domain.repository.IForecastRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ForecastRepository @Inject constructor(
    private val temperatureService: TemperatureService,
    private val citiesService: CitiesService,
    private val cityWeatherDao: CityWeatherDao,
    private val dispatcher: CoroutineDispatcher
) : IForecastRepository {
    private var addedCities: Set<CityWeather>? = null

    override suspend fun searchCity(query: String): Result<City> {
        return withContext(dispatcher) {
            runCatching {
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
        return withContext(dispatcher) {
            runCatching {
                temperatureService.searchTempAsync(
                    lat = city.coordinates.lat,
                    lon = city.coordinates.lon
                )
                    .await()
                    .takeIf { it.isSuccessful }
                    ?.body()
                    ?.toCityWeather(city, chosen = addedCities.isNullOrEmpty())
                    ?: throw Exception("empty data")
            }
        }
    }

    override suspend fun writeCityToBase(city: CityWeather) {
        withContext(dispatcher) {
            cityWeatherDao.insert(city = city.toCityWeatherEntity())
            insertInMemory(city = city)
        }
    }

    private fun insertInMemory(city: CityWeather) {
        addedCities = addedCities?.toMutableSet()?.let { cities ->
            cities.add(city)
            cities
        } ?: setOf(city)
    }

    override suspend fun updateCityInBase(city: CityWeather): Set<CityWeather> {
        withContext(dispatcher) {
            cityWeatherDao.update(city.toCityWeatherEntity())
            updateInMemory(city)
        }

        return addedCities ?: emptySet()
    }

    private fun updateInMemory(city: CityWeather) {
        addedCities = addedCities?.toMutableSet()?.let { cities ->
            val index = cities.indexOfFirst { it.id == city.id }
            val temp = cities.filter { it.id != city.id }.toMutableList()
            temp.add(index, city)
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

    override suspend fun deleteCityInBase(city: CityWeather): Set<CityWeather> {
        withContext(dispatcher) {
            cityWeatherDao.delete(cityID = city.id)
            deleteFromMemory(city = city)
        }

        return addedCities ?: emptySet()
    }

    override suspend fun getCityByID(cityID: String): CityWeather =
        withContext(dispatcher) {
            cityWeatherDao.getCityByID(cityID).toCityWeather()
        }


    private fun deleteFromMemory(city: CityWeather) {
        addedCities = addedCities?.toMutableSet()?.let { cities ->
            cities.remove(city)
            cities
        }
    }
}
package com.example.repository

import com.example.extensions.mappers.CityWeatherMappers.toCityWeatherEntity
import com.example.extensions.mappers.DtoMappers.toCity
import com.example.extensions.mappers.DtoMappers.toCityWeather
import com.example.extensions.mappers.EntityMappers.toCityWeather
import com.example.forecast.domain.model.CityToSearch
import com.example.forecast.domain.model.CityWeather
import com.example.forecast.domain.repository.IForecastRepository
import com.example.local.CityWeatherDao
import com.example.remote.services.CitiesService
import com.example.remote.services.TemperatureService
import kotlinx.coroutines.*
import javax.inject.Inject

class ForecastRepository @Inject constructor(
    private val temperatureService: TemperatureService,
    private val citiesService: CitiesService,
    private val cityWeatherDao: CityWeatherDao,
    private val dispatcher: CoroutineDispatcher
) : IForecastRepository {
    private var addedCities: Set<CityWeather>? = null

    init {
        GlobalScope.launch(dispatcher) {
            addedCities = getAll()
        }
    }

    override suspend fun searchCity(query: String): Result<CityToSearch> {
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

    override suspend fun searchForecastByCoordinates(cityToSearch: CityToSearch): Result<CityWeather> {
        return withContext(dispatcher) {
            runCatching {
                temperatureService.searchTempAsync(
                    lat = cityToSearch.coordinates!!.lat, //
                    lon = cityToSearch.coordinates!!.lon,
                )
                    .await()
                    .takeIf { it.isSuccessful }
                    ?.body()
                    ?.toCityWeather(cityToSearch)
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
            when (cities.add(city)) {
                true -> cities
                false -> {
                    val index = cities.indexOfFirst { it.id == city.id }
                    val temp = cities.filter { it.id != city.id }.toMutableList()
                    temp.add(index, city)
                    temp.toSet()
                }
            }
        } ?: setOf(city)
    }

    override suspend fun updateCityInBase(city: CityWeather) {
        withContext(dispatcher) {
            cityWeatherDao.update(city.toCityWeatherEntity())
            updateInMemory(city)
        }
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

    override suspend fun getCityByID(cityID: String): CityWeather? =
        withContext(dispatcher) {
            getAll()
            cityWeatherDao.getCityByID(cityID)?.toCityWeather()
        }


    private fun deleteFromMemory(city: CityWeather) {
        addedCities = addedCities?.toMutableSet()?.let { cities ->
            cities.remove(city)
            cities
        }
    }
}
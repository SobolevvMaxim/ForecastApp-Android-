package com.example.homeworksandroid.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homeworksandroid.App
import com.example.homeworksandroid.CityWeather
import com.example.homeworksandroid.repos.CitiesRepository
import com.example.homeworksandroid.repos.ForecastRepository
import kotlinx.coroutines.*

class CitiesViewModel : ViewModel() {
    private val citiesSearchRepos = CitiesRepository(App.citiesService)
    private val forecastSearchRepos = ForecastRepository(App.forecastService, App.getCityDao())
    private val exceptionHandler = CoroutineExceptionHandler { _, t ->
        _errorLiveData.postValue(t.toString())
    }

    private val _citiesLiveData = MutableLiveData<Set<CityWeather>>()
    val citiesLiveData: LiveData<Set<CityWeather>> get() = _citiesLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData

    private var searchJob: Job? = null

    override fun onCleared() {
        super.onCleared()

        searchJob = null
    }

    fun search(text: CharSequence) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(exceptionHandler) {
            delay(500)
            val cityWeatherResponse = citiesSearchRepos.search(text as String)
            cityWeatherResponse.getOrNull()?.let { city ->
                searchForecast(city)
            } ?: run {
                _errorLiveData.postValue(
                    cityWeatherResponse.exceptionOrNull()?.message ?: "unexpected exception"
                )
            }
        }
    }

    private fun searchForecast(city: CityWeather) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(exceptionHandler) {
            delay(500)
            val cityTemperatureResponse = forecastSearchRepos.searchTemp(city)
            cityTemperatureResponse.getOrNull()?.let {
                if (isDbEmpty()) city.chosen = true
                _citiesLiveData.postValue(forecastSearchRepos.writeCityToBase(city = city))
            } ?: run {
                _errorLiveData.postValue(
                    cityTemperatureResponse.exceptionOrNull()?.message ?: "unexpected exception"
                )
            }
        }
    }

    fun getAddedCities() {
        viewModelScope.launch {
            _citiesLiveData.postValue(forecastSearchRepos.getAll())
        }
    }

    fun changeChosenCities(lastChosenIndex: Int, newChosenIndex: Int) {
        viewModelScope.launch {
            forecastSearchRepos.changeChosenCityByName(lastChosenIndex, newChosenIndex)
            _citiesLiveData.postValue(forecastSearchRepos.getAll())
        }
    }

    private fun isDbEmpty(): Boolean = forecastSearchRepos.isDbEmpty()
}

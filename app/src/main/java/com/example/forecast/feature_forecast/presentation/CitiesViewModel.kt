package com.example.forecast.feature_forecast.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forecast.feature_forecast.data.repository.ForecastRepository
import com.example.forecast.feature_forecast.domain.model.City
import com.example.forecast.feature_forecast.domain.model.CityWeather
import com.example.forecast.feature_forecast.domain.use_case.GetCityInfo
import com.example.forecast.feature_forecast.domain.use_case.GetForecast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class CitiesViewModel @Inject constructor(
    private val getCityInfoUseCase: GetCityInfo,
    private val getForecastUseCase: GetForecast,
    private val forecastSearchRepos: ForecastRepository
) : ViewModel() {

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
            val cityWeatherResponse = getCityInfoUseCase(text as String)
            cityWeatherResponse.getOrNull()?.let { city ->
                searchForecast(city)
            } ?: run {
                _errorLiveData.postValue(
                    cityWeatherResponse.exceptionOrNull()?.message ?: "unexpected exception"
                )
            }
        }
    }

    private fun searchForecast(city: City) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(exceptionHandler) {
            delay(500)
            val cityTemperatureResponse = getForecastUseCase(city)
            cityTemperatureResponse.getOrNull()?.let {
                if (isDbEmpty()) it.chosen = true
                _citiesLiveData.postValue(forecastSearchRepos.writeCityToBase(city = it))
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

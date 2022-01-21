package com.example.forecast.feature_forecast.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forecast.feature_forecast.domain.model.City
import com.example.forecast.feature_forecast.domain.model.CityWeather
import com.example.forecast.feature_forecast.domain.repository.IForecastRepository
import com.example.forecast.feature_forecast.domain.use_case.DeleteCity
import com.example.forecast.feature_forecast.domain.use_case.GetCityInfo
import com.example.forecast.feature_forecast.domain.use_case.GetForecast
import com.example.forecast.feature_forecast.domain.use_case.UpdateCityForecast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CitiesViewModel @Inject constructor(
    private val getCityInfoUseCase: GetCityInfo,
    private val getForecastUseCase: GetForecast,
    private val deleteCityUseCase: DeleteCity,
    private val updateCityUseCase: UpdateCityForecast,
    private val forecastSearchRepos: IForecastRepository,
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

    fun searchCityForecastByName(text: CharSequence) {
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
                _citiesLiveData.postValue(forecastSearchRepos.writeCityToBase(city = it))
            } ?: run {
                _errorLiveData.postValue(
                    cityTemperatureResponse.exceptionOrNull()?.message ?: "unexpected exception"
                )
            }
        }
    }

    fun updateCityForecast(cityWeather: CityWeather) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(exceptionHandler) {
            val updatedCityResponse = getForecastUseCase(city = cityWeather.toCity())
            updatedCityResponse.getOrNull()?.let {
                it.chosen = true
                val cities = updateCityUseCase(it)
                _citiesLiveData.postValue(cities)
            } ?: kotlin.run {
                _errorLiveData.postValue(
                    updatedCityResponse.exceptionOrNull()?.message ?: "unexpected exception"
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

    fun deleteCity(city: CityWeather) {
        viewModelScope.launch(exceptionHandler) {
            val addedCities = deleteCityUseCase(city)
            _citiesLiveData.postValue(addedCities)
        }
    }
}
package com.example.forecast.feature_forecast.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forecast.domain.model.City
import com.example.forecast.domain.model.CityWeather
import com.example.forecast.domain.repository.IForecastRepository
import com.example.forecast.domain.use_case.*
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
    private val loadForecastsUseCase: LoadForecastsUseCase,
    private val changeChosenCityUseCase: ChangeChosenCityUseCase,
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, t ->
        _errorLiveData.postValue(t.toString())
    }

    private val _chosenLiveData = MutableLiveData<CityWeather?>()
    val chosenLiveData: LiveData<CityWeather?> get() = _chosenLiveData

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
            val cityInfoResponse = getCityInfoUseCase(text as String)
            cityInfoResponse.getOrNull()?.let { city ->
                searchForecast(city)
            } ?: run {
                _errorLiveData.postValue(
                    cityInfoResponse.exceptionOrNull()?.message ?: "unexpected exception"
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
                if (it.chosen) _chosenLiveData.postValue(it)
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
            } ?: run {
                _errorLiveData.postValue(
                    updatedCityResponse.exceptionOrNull()?.message ?: "unexpected exception"
                )
            }
        }
    }

    fun getAddedCities() {
        viewModelScope.launch {
            val forecasts = loadForecastsUseCase()
            _citiesLiveData.postValue(forecasts)
        }
    }

    fun changeChosenCity(lastChosenIndex: Int, newChosenIndex: Int) {
        viewModelScope.launch {
            changeChosenCityUseCase(lastChosenIndex, newChosenIndex)
            getAddedCities()
        }
    }

    fun deleteCity(city: CityWeather) {
        viewModelScope.launch(exceptionHandler) {
            val addedCities = deleteCityUseCase(city)
            _citiesLiveData.postValue(addedCities)
        }
    }

    fun getChosenCity() {
        viewModelScope.launch(exceptionHandler) {
            val chosenCity = forecastSearchRepos.getChosenCityFromBase()
            _chosenLiveData.postValue(chosenCity)
        }
    }
}
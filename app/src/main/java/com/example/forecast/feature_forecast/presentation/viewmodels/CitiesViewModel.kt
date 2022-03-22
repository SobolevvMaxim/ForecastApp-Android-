package com.example.forecast.feature_forecast.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.forecast.domain.model.CityWeather
import com.example.forecast.domain.use_case.DeleteCity
import com.example.forecast.domain.use_case.LoadForecasts
import com.example.forecast.feature_forecast.presentation.base.BaseViewModel
import com.example.forecast.feature_forecast.presentation.base.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CitiesViewModel @Inject constructor(
    private val deleteCityUseCase: DeleteCity,
    private val loadForecastsUseCase: LoadForecasts,
) : BaseViewModel() {

    private val _citiesLiveData = MutableLiveData<Event<Set<CityWeather>>>()
    val citiesLiveData: LiveData<Event<Set<CityWeather>>> get() = _citiesLiveData

    fun getAddedCities() {
        simpleRequest(
            request = {
                loadForecastsUseCase()
            },
            successCallback = { citiesFromBase ->
                _citiesLiveData.postValue(Event.Success(citiesFromBase))
            },
            errorCallback = { error ->
                _citiesLiveData.postValue(Event.Error(error))
            }
        )
    }

    fun deleteCity(cityToDelete: CityWeather) {
        simpleRequest(
            request = {
                deleteCityUseCase(cityToDelete)
            },
            successCallback = { cities ->
                _citiesLiveData.postValue(Event.Success(cities))
            },
            errorCallback = { error ->
                _citiesLiveData.postValue(Event.Error(error))
            }
        )
    }
}
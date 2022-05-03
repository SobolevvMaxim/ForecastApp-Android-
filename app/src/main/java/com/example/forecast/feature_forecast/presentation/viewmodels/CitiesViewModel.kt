package com.example.forecast.feature_forecast.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.forecast.domain.model.CityWeather
import com.example.forecast.domain.use_case.DeleteCity
import com.example.forecast.domain.use_case.LoadForecasts
import com.example.forecast.feature_forecast.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CitiesViewModel @Inject constructor(
    private val loadForecastsUseCase: LoadForecasts,
    private val deleteCityUseCase: DeleteCity,
) : BaseViewModel() {

    private val _citiesLiveData = MutableLiveData<Set<CityWeather>>()
    val citiesLiveData: LiveData<Set<CityWeather>> get() = _citiesLiveData

    fun getAddedCities() {
        simpleRequest(
            request = {
                loadForecastsUseCase()
            },
            successCallback = { citiesFromBase ->
                _citiesLiveData.postValue(citiesFromBase)
            }
        )
    }

    fun deleteCity(cityToDelete: CityWeather) {
        simpleRequest(
            request = {
                deleteCityUseCase(cityToDelete)
            },
            successCallback = { cities ->
                _citiesLiveData.postValue(cities)
            }
        )
    }
}
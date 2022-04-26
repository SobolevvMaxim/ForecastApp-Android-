package com.example.forecast.feature_forecast.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.forecast.domain.model.CityWeather
import com.example.forecast.domain.use_case.DeleteCity
import com.example.forecast.feature_forecast.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CitiesViewModel @Inject constructor(
    private val deleteCityUseCase: DeleteCity,
) : BaseViewModel() {

    private val _citiesLiveData = MutableLiveData<Set<CityWeather>>()
    val citiesLiveData: LiveData<Set<CityWeather>> get() = _citiesLiveData

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
package com.example.forecast.feature_forecast.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.forecast.domain.model.City
import com.example.forecast.domain.model.CityWeather
import com.example.forecast.domain.use_case.*
import com.example.forecast.feature_forecast.presentation.base.BaseViewModel
import com.example.forecast.feature_forecast.presentation.base.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CitiesViewModel @Inject constructor(
    private val getCityInfoUseCase: GetCityInfo,
    private val getForecastUseCase: GetForecast,
    private val deleteCityUseCase: DeleteCity,
    private val updateCityUseCase: UpdateCityForecast,
    private val loadForecastsUseCase: LoadForecastsUseCase,
    private val writeCityToBaseUseCase: WriteCityToBaseUseCase,
    private val getCityByIDUseCase: GetCityByIDUseCase,
) : BaseViewModel() {

    private val _chosenLiveData = MutableLiveData<Event<CityWeather>>()
    val chosenLiveData: LiveData<Event<CityWeather>> get() = _chosenLiveData

    private val _citiesLiveData = MutableLiveData<Event<Set<CityWeather>>>()
    val citiesLiveData: LiveData<Event<Set<CityWeather>>> get() = _citiesLiveData

    fun searchCityForecastByName(searchInput: CharSequence) {
        _chosenLiveData.postValue(Event.Loading())
        networkRequest(
            request = {
                getCityInfoUseCase(searchInput as String)
            },
            successCallback = { city ->
                searchForecast(city)
            },
            errorCallback = { error ->
                _chosenLiveData.postValue(Event.Error(error))
            }
        )
    }

    private fun searchForecast(cityToSearchForecast: City) {
        _chosenLiveData.postValue(Event.Loading())
        networkRequest(
            request = {
                getForecastUseCase(cityToSearchForecast)
            },
            successCallback = { cityForecast ->
                writeCityToBaseUseCase(cityForecast)
                _chosenLiveData.postValue(Event.Success(cityForecast))
            },
            errorCallback = { error ->
                _chosenLiveData.postValue(Event.Error(error))
            }
        )
    }

    fun updateCityForecast(cityToUpdate: CityWeather) {
        _chosenLiveData.postValue(Event.Loading())
        networkRequest(
            request = {
                getForecastUseCase(city = cityToUpdate.toCity())
            },
            successCallback = { updatedCity ->
                updateCityUseCase(updatedCity)
                _chosenLiveData.postValue(Event.Success(updatedCity))
            },
            errorCallback = { error ->
                _chosenLiveData.postValue(Event.Error(error))
            }
        )
    }

    fun getAddedCities(postResults: Boolean) {
        when (postResults) {
            true -> simpleRequest(
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
            false -> simpleRequest(
                request = {
                    loadForecastsUseCase()
                },
                errorCallback = { error ->
                    _citiesLiveData.postValue(Event.Error(error))
                }
            )
        }
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

    fun getCityByID(cityID: String) {
        simpleRequest(
            request = {
                getCityByIDUseCase(cityID)
            },
            successCallback = { cityByID ->
                _chosenLiveData.postValue(Event.Success(cityByID))
            },
            errorCallback = { error ->
                _chosenLiveData.postValue(Event.Error(error))
            }
        )
    }
}
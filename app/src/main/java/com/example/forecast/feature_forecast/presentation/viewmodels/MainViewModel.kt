package com.example.forecast.feature_forecast.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.extensions.mappers.CityWeatherMappers.toCityToSearch
import com.example.forecast.domain.model.CityToSearch
import com.example.forecast.domain.model.CityWeather
import com.example.forecast.domain.prefstore.IPrefStore
import com.example.forecast.domain.use_case.*
import com.example.forecast.feature_forecast.presentation.base.BaseViewModel
import com.example.forecast.feature_forecast.presentation.base.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCityInfoUseCase: GetCityInfo,
    private val getForecastUseCase: GetForecast,
    private val updateCityUseCase: UpdateCityInBase,
    private val writeCityToBaseUseCase: WriteCityToBase,
    private val getCityUseCase: GetCityByID,
    private val prefStore: IPrefStore,
) : BaseViewModel() {

    private val _chosenLiveData = MutableLiveData<Event<CityWeather>>()
    val chosenLiveData: LiveData<Event<CityWeather>> get() = _chosenLiveData

    val chosenID = prefStore.getChosen().asLiveData()

    fun searchCityInfoByName(cityToSearch: CityToSearch) {
        _chosenLiveData.postValue(Event.Loading())
        networkRequest(
            request = {
                Timber.d("Searching city info by name: %s", cityToSearch)
                getCityInfoUseCase(cityToSearch.searchName)
            },
            successCallback = { city ->
                searchForecastByCoordinates(city)
            },
            errorCallback = { error ->
                _chosenLiveData.postValue(Event.Error(error))
            }
        )
    }

    fun searchForecastByCoordinates(cityToSearch: CityToSearch) {
        _chosenLiveData.postValue(Event.Loading())
        networkRequest(
            request = {
                Timber.d("Searching city forecast by coordinates: %s", cityToSearch)
                getForecastUseCase(cityToSearch)
            },
            successCallback = { cityForecast ->
                writeCityToBaseUseCase(cityForecast)
                _chosenLiveData.postValue(Event.Success(cityForecast))
            },
            errorCallback = {
                _chosenLiveData.postValue(Event.Error(it))
            }
        )
    }

    fun updateCityForecast(cityToUpdate: CityWeather) {
        _chosenLiveData.postValue(Event.Loading())
        networkRequest(
            request = {
                Timber.d("Updating city forecast: %s", cityToUpdate)
                getForecastUseCase(cityToUpdate.toCityToSearch())
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

    fun getCityByID(cityID: String) {
        simpleRequest(
            request = {
                Timber.d("Getting city by id: %s", cityID)
                getCityUseCase(cityID)
            },
            successCallback = { cityByID ->
                cityByID?.let {
                    _chosenLiveData.postValue(Event.Success(it))
                }
            },
            errorCallback = { error ->
                _chosenLiveData.postValue(Event.Error(error))
            }
        )
    }

    fun changeChosenInBase(newChosenID: String) {
        simpleRequest(
            request = {
                Timber.d("Changing chosen in base, new id: %s", newChosenID)
                prefStore.changeChosen(newChosenID)
            }
        )
    }
}
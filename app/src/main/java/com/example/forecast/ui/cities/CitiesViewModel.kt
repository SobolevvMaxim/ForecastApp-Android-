package com.example.forecast.ui.cities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.forecast.domain.model.CityWeather
import com.example.forecast.domain.prefstore.IPrefStore
import com.example.forecast.domain.use_case.DeleteCity
import com.example.forecast.domain.use_case.LoadForecasts
import com.example.forecast.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CitiesViewModel @Inject constructor(
    private val loadForecastsUseCase: LoadForecasts,
    private val deleteCityUseCase: DeleteCity,
    private val prefStore: IPrefStore,
) : BaseViewModel() {

    private val _citiesLiveData = MutableLiveData<Set<CityWeather>>()
    val citiesLiveData: LiveData<Set<CityWeather>> get() = _citiesLiveData

    val chosenID = prefStore.getChosen().asLiveData()

    fun getAddedCities() {
        simpleRequest(
            request = {
                Timber.d("Getting added cities from base...")
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
                Timber.d("Deleting city: %s", cityToDelete)
                deleteCityUseCase(cityToDelete)
            },
            successCallback = { cities ->
                _citiesLiveData.postValue(cities)
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
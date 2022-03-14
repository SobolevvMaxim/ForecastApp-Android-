package com.example.forecast.feature_forecast.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.forecast.feature_forecast.presentation.base.BaseViewModel
import com.example.forecast.feature_forecast.presentation.base.Event
import com.example.forecast.domain.model.City
import com.example.forecast.domain.model.CityWeather
import com.example.forecast.domain.use_case.*
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

    fun searchCityForecastByName(text: CharSequence) {
        networkRequest(
            liveData = null,
            request = {
                getCityInfoUseCase(text as String)
            },
            successCallback = {
                searchForecast(it)
            }
        )
    }

    private fun searchForecast(city: City) {
        networkRequest(
            _chosenLiveData,
            request = {
                getForecastUseCase(city)
            },
            successCallback = {
                writeCityToBaseUseCase(it)
            }
        )
    }

    fun updateCityForecast(cityWeather: CityWeather) {
        networkRequest(
            liveData = _chosenLiveData,
            request = {
                getForecastUseCase(city = cityWeather.toCity())
            },
            successCallback = {
                updateCityUseCase(it)
            }
        )
    }

    fun getAddedCities(post: Boolean) {
        when (post) {
            true -> simpleRequest(
                liveData = _citiesLiveData,
                request = {
                    loadForecastsUseCase()
                }
            )
            false -> simpleRequest(
                liveData = null,
                request = {
                    loadForecastsUseCase
                }
            )
        }
    }

    fun deleteCity(city: CityWeather) {
        simpleRequest(
            liveData = _citiesLiveData,
            request = {
                deleteCityUseCase(city)
            }
        )
    }

    fun getCityByID(cityID: String) {
        simpleRequest(
            liveData = _chosenLiveData,
            request = {
                getCityByIDUseCase(cityID)
            }
        )
    }
}
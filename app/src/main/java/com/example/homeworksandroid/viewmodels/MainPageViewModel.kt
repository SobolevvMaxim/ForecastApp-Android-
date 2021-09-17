package com.example.homeworksandroid.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homeworksandroid.App
import com.example.homeworksandroid.CityWeather
import com.example.homeworksandroid.repos.ForecastRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainPageViewModel : ViewModel() {
    private val forecastRepository = ForecastRepository(App.forecastService, App.getCityDao())
    private val exceptionHandler = CoroutineExceptionHandler { _, t ->
        _errorLiveData.postValue(t.toString())
    }

    private val _citiesLiveData = MutableLiveData<CityWeather>()
    val citiesLiveData: LiveData<CityWeather> get() = _citiesLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData

    private var dbJob: Job? = null

    override fun onCleared() {
        super.onCleared()

        dbJob = null
    }

    fun getCurrentCity() {
        dbJob?.cancel()
        dbJob = viewModelScope.launch(exceptionHandler) {
            delay(500)
            val cityWeatherResponse = forecastRepository.getChosenCity()
            Log.d("MY_ERROR", "city got $cityWeatherResponse")
            _citiesLiveData.postValue(cityWeatherResponse)
        }
    }

    fun isDbEmpty(): Boolean = forecastRepository.isDbEmpty()
}
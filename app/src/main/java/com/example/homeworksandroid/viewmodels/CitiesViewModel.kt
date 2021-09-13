package com.example.homeworksandroid.viewmodels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homeworksandroid.App
import com.example.homeworksandroid.CityWeather
import com.example.homeworksandroid.repos.CitiesRepository
import kotlinx.coroutines.*

class CitiesViewModel : ViewModel() {
    private val citiesSearchRepos = CitiesRepository(App.citiesService)
    private val exceptionHandler = CoroutineExceptionHandler { _, t ->
        _errorLiveData.postValue(t.toString())
    }

    private val _citiesLiveData = MutableLiveData<LinkedHashSet<CityWeather>>()
    val citiesLiveData: LiveData<LinkedHashSet<CityWeather>> get() = _citiesLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData


    private var searchJob: Job? = null

    override fun onCleared() {
        super.onCleared()

        searchJob = null
    }

    fun search(text: CharSequence) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(exceptionHandler) {
            delay(500)
            Log.d("MY_ERROR", "search: $text")
            val cityWeatherResponse = citiesSearchRepos.search(text as String)
            cityWeatherResponse.getOrNull()?.let { city ->
                addCity(name = text, cityWeather = city)
            } ?: run {
                _errorLiveData.postValue(
                    cityWeatherResponse.exceptionOrNull()?.message ?: "unexpected exception"
                )
            }
        }
    }

    private fun addCity(name: String, cityWeather: CityWeather): Boolean {
        if (_citiesLiveData.value?.any { it.name.contentEquals(name) } == true || name.isBlank()) {
            return false
        }
        _citiesLiveData.value?.let {
            it.add(cityWeather)
            _citiesLiveData.postValue(it)
        } ?: _citiesLiveData.postValue(linkedSetOf(cityWeather))

        return true

    }
}

package com.example.forecast.feature_forecast.presentation.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private var searchJob: Job? = null

    override fun onCleared() {
        super.onCleared()

        searchJob = null
    }

    fun <T> networkRequest(
        liveData: MutableLiveData<Event<T>>?,
        request: suspend () -> Result<T>,
        successCallback: suspend (T) -> Unit,
    ) {
        liveData?.postValue(Event.Loading())
        searchJob = null


        searchJob = viewModelScope.launch {
            delay(500)
            try {
                val response = request.invoke()
                response.getOrNull()?.let { data ->
                    successCallback(data)
                    liveData?.postValue(Event.Success(data))
                } ?: run {
                    liveData?.postValue(Event.Error(response.exceptionOrNull()))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                liveData?.postValue(Event.Error(e))
            }
        }
    }

    fun <T> simpleRequest(
        liveData: MutableLiveData<Event<T>>?,
        request: suspend () -> T?,
    ) {
        liveData?.postValue(Event.Loading())
        viewModelScope.launch {
            try {
                val response = request.invoke()
                response?.let {
                    liveData?.postValue(Event.Success(it))
                } ?: run {
                    liveData?.postValue(Event.Error(null))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                liveData?.postValue(Event.Error(e))
            }
        }
    }
}

sealed class Event<T> {
    class Loading<T>: Event<T>()
    class Success<T>(val data: T?): Event<T>()
    class Error<T>(val throwable: Throwable?): Event<T>()
}

inline fun <reified T> isNullable(): Boolean = null is T
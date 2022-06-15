package com.example.forecast.feature_forecast.base

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
        request: suspend () -> Result<T>,
        successCallback: (suspend (T) -> Unit)? = null,
        errorCallback: (Throwable?) -> Unit,
    ) {
        searchJob = null


        searchJob = viewModelScope.launch {
            delay(500)
            try {
                val response = request.invoke()
                response.getOrNull()?.let { data ->
                    successCallback?.invoke(data)
                } ?: run {
                    errorCallback(response.exceptionOrNull())
                }
            } catch (e: Exception) {
                e.printStackTrace()
                errorCallback(e)
            }
        }
    }

    fun <T> simpleRequest(
        request: suspend () -> T?,
        successCallback: ((T?) -> Unit)? = null,
        errorCallback: ((Throwable?) -> Unit)? = null,
    ) {
        viewModelScope.launch {
            try {
                val response = request.invoke()
                successCallback?.invoke(response)

            } catch (e: Exception) {
                e.printStackTrace()
                errorCallback?.invoke(e)
            }
        }
    }
}
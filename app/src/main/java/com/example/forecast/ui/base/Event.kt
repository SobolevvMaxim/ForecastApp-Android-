package com.example.forecast.ui.base

sealed class Event<T> {
    class Loading<T> : Event<T>()
    class Success<T>(val data: T) : Event<T>()
    class Error<T>(val throwable: Throwable?) : Event<T>()
}
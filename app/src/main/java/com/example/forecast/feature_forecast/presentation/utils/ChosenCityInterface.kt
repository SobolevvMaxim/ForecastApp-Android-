package com.example.forecast.feature_forecast.presentation.utils

interface ChosenCityInterface {
    fun changeChosenInBase(newChosenID: String)

    fun getChosenCityID(): String
}
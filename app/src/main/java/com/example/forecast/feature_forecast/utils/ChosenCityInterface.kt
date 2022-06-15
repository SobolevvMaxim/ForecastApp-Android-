package com.example.forecast.feature_forecast.utils

interface ChosenCityInterface {
    fun changeChosenCityID(newChosenID: String)

    fun getChosenCityID(): String
}
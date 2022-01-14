package com.example.forecast.feature_forecast.presentation

import androidx.fragment.app.Fragment

interface NavigationHost {
    fun navigateTo(fragment: Fragment, addToBackstack: Boolean)
}
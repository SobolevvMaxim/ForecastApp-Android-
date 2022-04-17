package com.example.forecast.feature_forecast.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.lifecycle.Observer
import com.example.forecast.R
import com.example.forecast.domain.model.CityWeather
import com.example.forecast.feature_forecast.presentation.utils.ChosenCityInterface
import com.example.forecast.feature_forecast.presentation.viewmodels.ActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.main_app_bar.*

@AndroidEntryPoint
class AppActivity : AppCompatActivity(R.layout.main_activity), ChosenCityInterface {
    private val viewModel: ActivityViewModel by viewModels()

    private val citiesObserver = Observer<Set<CityWeather>> { cities ->
        cities.run {
            if (this.isNullOrEmpty())
                return@Observer

            addCitiesToMenu(this.toList())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(topAppBar)

        viewModel.citiesLiveData.observe(this, citiesObserver)

        navigation.setNavigationItemSelectedListener { tappedItem ->
            changeMenuChecked(tappedItem)

            viewModel.citiesLiveData.value?.firstOrNull { it.name == tappedItem.title }?.let {
                changeChosenInBase(it.id)
            }

            drawer.close()
            true
        }

        topAppBar.setNavigationOnClickListener {
            Log.d(getString(R.string.main_log), "Navigation Pressed!")
            drawer.open()
            viewModel.getAddedCities()
        }
    }

    private fun changeMenuChecked(newChecked: MenuItem) {
        navigation.menu.forEach { item -> item.isChecked = false }
        newChecked.isChecked = true
    }

    private fun addCitiesToMenu(cities: List<CityWeather>) {
        val navMenu = navigation.menu
        val currentChosenID = getChosenCityID()
        navMenu.clear()
        cities.forEach {
            val item = navMenu.add(R.id.group1, it.id.toInt(), Menu.NONE, it.name)

            if (it.id == currentChosenID) item.isChecked = true
        }
    }

    override fun changeChosenInBase(newChosenID: String) {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(getString(R.string.chosen_pref_key), newChosenID)
            apply()
        }
        Log.d(getString(R.string.main_log), "Changing chosen in base (AppActivity): $newChosenID")
    }

    override fun getChosenCityID(): String {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        val defaultValue = resources.getString(R.string.default_chosen_id)
        val chosenIndex = sharedPref.getString(getString(R.string.chosen_pref_key), defaultValue)
        Log.d(getString(R.string.main_log), "Getting chosen from base (AppActivity): $chosenIndex")
        return chosenIndex ?: "0"
    }
}
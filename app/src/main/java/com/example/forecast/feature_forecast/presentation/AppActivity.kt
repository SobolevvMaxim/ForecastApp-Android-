package com.example.forecast.feature_forecast.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import com.example.forecast.R
import com.example.forecast.domain.model.CityWeather
import com.example.forecast.feature_forecast.presentation.base.Event
import com.example.forecast.feature_forecast.presentation.utils.ChosenCityInterface
import com.example.forecast.feature_forecast.presentation.viewmodels.ActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.main_app_bar.*

@AndroidEntryPoint
class AppActivity : AppCompatActivity(R.layout.main_activity),
    ChosenCityInterface {
    private val viewModel: ActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(topAppBar)

        viewModel.citiesLiveData.observe(this) { cities ->
            when (cities) {
                is Event.Loading -> {}
                is Event.Success<Set<CityWeather>> -> {
                    cities.data.run {
                        if (this.isNullOrEmpty())
                            return@observe

                        val navMenu = navigation.menu
                        val currentChosenID = getChosenCityID()
                        navMenu.clear()
                        this.forEach {
                            val item = navMenu.add(R.id.group1, it.id.toInt(), Menu.NONE, it.name)
                            when (it.id == currentChosenID) {
                                true -> item.isChecked = true
                                false -> {}
                            }
                        }
                    }
                }
                is Event.Error -> {}
            }
        }

        navigation.setNavigationItemSelectedListener { tappedItem ->
            navigation.menu.forEach { item -> item.isChecked = false }
            tappedItem.isChecked = true
            viewModel.citiesLiveData.value.apply {
                when (this) {
                    is Event.Success<Set<CityWeather>> -> {
                        this.data?.firstOrNull { it.name == tappedItem.title }?.id?.let {
                            changeChosenInBase(it)
                        }
                    }
                    else -> {}
                }
            }

            drawer.close()
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                Log.d(getString(R.string.main_log), "Navigation Pressed!")
                drawer.open()
                viewModel.getAddedCities()
                true
            }
            else -> false
        }
    }

    override fun changeChosenInBase(newChosenID: String) {
        val sharedPref = this.getSharedPreferences("TEST", Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(getString(R.string.chosen_pref_key), newChosenID)
            apply()
        }
        Log.d(getString(R.string.main_log), "Changing chosen in base (AppActivity): $newChosenID")
    }

    override fun getChosenCityID(): String {
        val sharedPref = this.getSharedPreferences("TEST", Context.MODE_PRIVATE)
        val defaultValue = resources.getString(R.string.default_chosen_id)
        val chosenIndex = sharedPref.getString(getString(R.string.chosen_pref_key), defaultValue)
        Log.d(getString(R.string.main_log), "Getting chosen from base (AppActivity): $chosenIndex")
        return chosenIndex ?: "0"
    }
}
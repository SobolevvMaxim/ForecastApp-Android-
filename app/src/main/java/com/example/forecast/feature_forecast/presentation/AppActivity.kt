package com.example.forecast.feature_forecast.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.forecast.R
import com.example.forecast.feature_forecast.presentation.utils.ChosenCityInterface
import com.example.forecast.feature_forecast.presentation.viewmodels.ActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppActivity : AppCompatActivity(R.layout.main_activity), ChosenCityInterface {
    private val viewModel: ActivityViewModel by viewModels()

//    private val navMenu by lazy {
//        navigation.menu
//    }

//    private val mainDrawer by lazy {
//        drawer
//    }

//    private val citiesObserver = Observer<Set<CityWeather>> { cities ->
//        cities.run {
//            if (this.isNullOrEmpty())
//                return@Observer
//
//            addCitiesToMenu(this.toList())
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setSupportActionBar(topAppBar)

//        viewModel.citiesLiveData.observe(this, citiesObserver)
//
//        // TODO: delete screen (with selecting?, adding city?) or long click listener on menu items
//        navigation.setNavigationItemSelectedListener { tappedItem ->
//            changeMenuChecked(tappedItem)
//
//            viewModel.citiesLiveData.value?.firstOrNull { it.name == tappedItem.title }?.let {
//                changeChosenInBase(it.id)
//            }
//
//            findNavController(R.id.nav_host_fragment).run {
//                if (currentDestination?.id != R.id.mainPageFragment)
//                    navigate(R.id.action_manageCitiesFragment_to_mainPageFragment)
//            }
//            mainDrawer.close()
//            true
//        }
//
//        topAppBar.setNavigationOnClickListener {
//            drawer.open()
//            viewModel.getAddedCities()
//        }
    }

    override fun onStart() {
        super.onStart()

//        viewModel.getAddedCities()
    }

//    private fun changeMenuChecked(newChecked: MenuItem) {
//        navMenu.forEach { item -> item.isChecked = false }
//        newChecked.isChecked = true
//    }
//
//    private fun addCitiesToMenu(cities: List<CityWeather>) {
//        val currentChosenID = getChosenCityID()
//        navMenu.clear()
//        cities.forEach {
//            val item = navMenu.add(R.id.cities, it.id.toInt(), Menu.NONE, it.name)
//
//            if (it.id == currentChosenID) item.isChecked = true
//        }
//        navigationOptions()
//    }

//    private fun navigationOptions() {
//        navMenu.add(getString(R.string.manage_cities)).setOnMenuItemClickListener {
//            findNavController(R.id.nav_host_fragment).run {
//                if (currentDestination?.id != R.id.manageCitiesFragment)
//                    navigate(R.id.action_mainPageFragment_to_manageCitiesFragment)
//            }
//            mainDrawer.close()
//            true
//        }
//        navMenu.add("TODO").setOnMenuItemClickListener {
//            Toast.makeText(this, "TODO pressed!", Toast.LENGTH_SHORT).show()
//            true
//        }
//    }

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
package com.example.forecast.feature_forecast.presentation.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.forecast.R
import com.example.forecast.checkNetwork
import com.example.forecast.domain.model.CityWeather
import com.example.forecast.feature_forecast.presentation.ChosenCityInterface
import com.example.forecast.feature_forecast.presentation.CitiesViewModel
import com.example.forecast.feature_forecast.presentation.NavigationHost
import com.example.forecast.feature_forecast.presentation.P_LOG
import com.example.forecast.feature_forecast.presentation.adapters.WeekForecastAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.main_page_fragment.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainPageFragment : Fragment(R.layout.main_page_fragment) {
    companion object {
        fun create() = MainPageFragment()
    }

    @Inject
    lateinit var dateFormat: SimpleDateFormat

    private val viewModel by viewModels<CitiesViewModel>({ requireActivity() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            viewModel.getAddedCities()
        }

        viewModel.citiesLiveData.observe(viewLifecycleOwner) { cities ->
            if (cities.isNullOrEmpty()) {
                viewModel.searchCityForecastByName(getString(R.string.default_city))
            } else {
                val chosenCity =
                    cities.firstOrNull { it.id == (activity as ChosenCityInterface).getChosenCityID() }
                chosenCity?.let {
                    updateView(it)
                } ?: run {
                    val firstElement = cities.toList()[0]
                    (activity as ChosenCityInterface).changeChosenInBase(firstElement.id)
                    updateView(firstElement)
                }
            }
        }

        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            Log.d(P_LOG, "ERROR:$it")
            Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_LONG).show()
        }

        mainAddButton.setOnClickListener {
            (activity as NavigationHost).navigateTo(CitiesFragment.create(), true)
        }
    }

    private fun updateView(city: CityWeather) {
        progress_bar_city.visibility = View.INVISIBLE

        city.apply {
            val cityInfoText = "$name, $country"
            currentCity.text = cityInfoText
            temperatures[0].run {
                temperature_today.text = temp.toString()
                description_today.text = description
            }
            currentDate.text = forecastDate
            setRecyclerView(this)
        }
    }

    private fun setRecyclerView(city: CityWeather) {
        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        forecast_recycler.layoutManager = layoutManager
        val date: Date = dateFormat.parse(city.forecastDate) ?: Date(1)

        val forecastAdapter =
            WeekForecastAdapter(
                city.temperatures.subList(1, city.temperatures.size),
                date,
                dateFormat
            )
        forecast_recycler.adapter = forecastAdapter
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResume() {
        super.onResume()

        if (!checkNetwork(context)) offline_mode.visibility =
            View.GONE else offline_mode.visibility = View.VISIBLE
    }
}
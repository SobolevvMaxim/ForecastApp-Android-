package com.example.forecast.feature_forecast.presentation.fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.text.trimmedLength
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
import kotlinx.android.synthetic.main.add_city_dialog.*
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

        viewModel.getAddedCities()

        val chosenCityID = (activity as ChosenCityInterface).getChosenCityID()

        if (savedInstanceState == null) {
            viewModel.getCityByID(cityID = chosenCityID)
        }

        viewModel.chosenLiveData.observe(viewLifecycleOwner) { city ->
            city?.let {
                updateView(it)
                (activity as ChosenCityInterface).changeChosenInBase(it.id)
            } ?: run {
                updateProgressBar(true)
                viewModel.searchCityForecastByName(getString(R.string.default_city))
            }
        }

        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            Log.d(P_LOG, "ERROR:$it")
            Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_LONG).show()
        }

        menu_button.setOnClickListener {
            (activity as NavigationHost).navigateToCitiesFragment()
        }

        mainAddButton.setOnClickListener {
            addCityDialog()
        }
    }

    @SuppressLint("InflateParams")
    private fun addCityDialog() {
        AlertDialog.Builder(requireContext()).create().apply {
            val inflater = requireActivity().layoutInflater
            setView(inflater.inflate(R.layout.add_city_dialog, null))
            setButton(AlertDialog.BUTTON_POSITIVE, "OK") { _, _ ->
                val cityInput = city_edit_text.text.toString()

                when (cityInput.trimmedLength()) {
                    in 0..3 -> Toast.makeText(
                        requireContext(),
                        "Incorrect input!",
                        Toast.LENGTH_LONG
                    ).show()
                    else -> {
                        viewModel.searchCityForecastByName(cityInput)
                        updateProgressBar(true)
                    }
                }
            }
            setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel") { dialog, _ ->
                dialog.cancel()
            }
            show()
        }
    }

    private fun updateProgressBar(visible: Boolean) {
        when (visible) {
            true -> loading_city_progress.visibility = View.VISIBLE
            false -> loading_city_progress.visibility = View.GONE
        }
    }

    private fun updateView(city: CityWeather) {
        updateProgressBar(false)

        city.apply {
            val cityInfoText = "$name, $country"
            currentCity.text = cityInfoText
            temperatures[0].run {
                val temperature = "$temp°"
                temperature_today.text = temperature
                description_today.text = description
            }
            currentDate.text = forecastDate
            setRecyclerView(this)
        }
    }

    private fun setRecyclerView(city: CityWeather) {
        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        forecast_recycler.layoutManager = layoutManager
        val date: Date = dateFormat.parse(city.forecastDate) ?: Date(1)

        val calendar = Calendar.getInstance()

        calendar.time = date
        val todayDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        val forecastAdapter =
            WeekForecastAdapter(
                city.temperatures.subList(1, city.temperatures.size),
                todayDayOfWeek
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
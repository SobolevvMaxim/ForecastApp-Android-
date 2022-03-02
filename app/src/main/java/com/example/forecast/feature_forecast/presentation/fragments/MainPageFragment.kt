package com.example.forecast.feature_forecast.presentation.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.text.trimmedLength
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.forecast.R
import com.example.forecast.di.DateFormat
import com.example.forecast.di.TimeFormat
import com.example.forecast.domain.model.CityWeather
import com.example.forecast.feature_forecast.presentation.CitiesViewModel
import com.example.forecast.feature_forecast.presentation.P_LOG
import com.example.forecast.feature_forecast.presentation.adapters.DayForecastAdapter
import com.example.forecast.feature_forecast.presentation.adapters.WeekForecastAdapter
import com.example.forecast.feature_forecast.presentation.utils.ChosenCityInterface
import com.example.forecast.feature_forecast.presentation.utils.NavigationHost
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
    @DateFormat
    lateinit var dateFormat: SimpleDateFormat

    @Inject
    @TimeFormat
    lateinit var timeFormat: SimpleDateFormat

    private val viewModel by viewModels<CitiesViewModel>({ requireActivity() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkNetwork(context)

        viewModel.getAddedCities()

        val chosenCityID = (activity as ChosenCityInterface).getChosenCityID()

        if (savedInstanceState == null) {
            viewModel.getCityByID(cityID = chosenCityID)
        }

        viewModel.chosenLiveData.observe(viewLifecycleOwner) { city ->
            swipe_layout.isRefreshing = false
            city?.let {
                Log.d("HOUR", "onViewCreated: get city: $it")
                checkToUpdate(it)
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
            showCitiesFragment()
        }

        mainAddButton.setOnClickListener {
            addCityDialog()
        }

        swipe_layout.setOnRefreshListener {
            currentCity.text?.let {
                viewModel.searchCityForecastByName(it.subSequence(0, it.length - 4))
            }
        }
    }

    private fun checkToUpdate(city: CityWeather) {
        val cityDate = Calendar.getInstance()
        val currentDate = Calendar.getInstance()

        cityDate.apply {
            time = getCityForecastDate(city)
            add(Calendar.HOUR, 1)
        }

        if (cityDate.time.before(currentDate.time) && networkAvailable()) {
            updateCityForecast(city)
        }
    }

    private fun updateCityForecast(city: CityWeather) {
        viewModel.updateCityForecast(city)
        updateProgressBar(true)
        Log.d("UPDATE", "Updating forecast...")
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
                        when (networkAvailable()) {
                            true -> {
                                viewModel.searchCityForecastByName(cityInput)
                                updateProgressBar(true)
                            }
                            false -> Toast.makeText(
                                context,
                                "Network unavailable now!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
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
            false -> loading_city_progress.visibility = View.INVISIBLE
        }
    }

    private fun showCitiesFragment() {
        (activity as NavigationHost).navigateToCitiesFragment()
    }

    private fun updateView(city: CityWeather) {
        updateProgressBar(false)

        city.apply {
            val cityInfoText = "$name, $country"
            currentCity.text = cityInfoText
            dailyTemperatures[0].run {
                when (description) {
                    "Rain" -> big_image.setImageResource(R.drawable.forecast_rain_icon)
                    "Snow" -> big_image.setImageResource(R.drawable.forecast_snow_icon)
                    "Clear" -> big_image.setImageResource(R.drawable.forecast_sun_icon)
                    else -> big_image.setImageResource(R.drawable.forecast_clouds_icon)
                }
                val temperature = "$temp°"
                temperature_today.text = temperature
                description_today.text = description
            }
            uvindex_value.text = uvi.toString()
            sunrise_value.text = getTime(city.sunrise)
            sunset_value.text = getTime(city.sunset)
            currentDate.text = forecastDate
            val humidityValue = "$humidity%"
            humidity_value.text = humidityValue

            setDailyRecyclerView(this)
            setHourlyRecyclerView(this)
        }
    }

    private fun setDailyRecyclerView(city: CityWeather) {
        val recyclerManager: RecyclerView.LayoutManager =
            object : LinearLayoutManager(context, RecyclerView.VERTICAL, false) {
                override fun canScrollVertically(): Boolean = false
            }

        daily_forecast_recycler.layoutManager = recyclerManager

        val date: Date = dateFormat.parse(city.forecastDate) ?: Date(1)
        val calendar = Calendar.getInstance()

        calendar.time = date
        val todayDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        val forecastAdapter =
            WeekForecastAdapter(
                city.dailyTemperatures.subList(1, city.dailyTemperatures.size),
                todayDayOfWeek,
                resources.getStringArray(R.array.days).toList()
            )
        daily_forecast_recycler.adapter = forecastAdapter
    }

    private fun setHourlyRecyclerView(city: CityWeather) {
        val recyclerManager: RecyclerView.LayoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        hourly_forecast_recycler.layoutManager = recyclerManager

        val date: Date = dateFormat.parse(city.forecastDate) ?: Date(1)
        val calendar = Calendar.getInstance()

        calendar.time = date
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)

        val forecastAdapter =
            DayForecastAdapter(
                city.hourlyTemperatures,
                hourOfDay,
            )
        hourly_forecast_recycler.adapter = forecastAdapter
    }

    private fun checkNetwork(context: Context?) {
        val manager: ConnectivityManager =
            context?.applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val builder = NetworkRequest.Builder()

            builder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI)

            val networkRequest = builder.build()
            manager.registerNetworkCallback(networkRequest,
                object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        super.onAvailable(network)
                        onChangeNetworkState(true)
                    }

                    override fun onUnavailable() {
                        super.onUnavailable()
                        onChangeNetworkState(false)
                    }

                    override fun onLost(network: Network) {
                        super.onLost(network)
                        onChangeNetworkState(false)
                    }
                })

        }
    }

    fun onChangeNetworkState(available: Boolean) {
        activity?.runOnUiThread {
            when (available) {
                true -> offline_mode.visibility = View.GONE
                false -> offline_mode.visibility = View.VISIBLE
            }
        }
    }

    private fun networkAvailable(): Boolean = !offline_mode.isVisible

    private fun getCityForecastDate(city: CityWeather) =
        dateFormat.parse(city.forecastDate) ?: Date(1)

    private fun getTime(time: String): String = timeFormat.format(Date(time.toLong()))
}